package com.example.tennisapp.domain.court.service;

import com.example.tennisapp.domain.court.dto.request.CourtCreate;
import com.example.tennisapp.domain.court.dto.request.CourtUpdate;
import com.example.tennisapp.domain.court.dto.response.CourtResponse;
import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.domain.court.repository.CourtRepository;
import com.example.tennisapp.domain.courtImage.repository.CourtImageRepository;
import com.example.tennisapp.domain.courtImage.entity.CourtImage;
import com.example.tennisapp.domain.owner.entity.Owner;
import com.example.tennisapp.domain.owner.repository.OwnerRepository;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourtService {

    private final CourtRepository courtRepository;
    private final OwnerRepository ownerRepository;
    private final S3Service s3Service;
    private final CourtImageRepository courtImageRepository;

    // Redis + Redisson
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    private static final String COURT_CACHE_PREFIX = "court:";
    private static final String COURTS_CACHE_KEY = "courts:all";

    @Transactional
    public CourtResponse createCourt(CourtCreate request) {

        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.OWNER_NOT_FOUND));

        if (courtRepository.existsByNameAndAddress(request.getName(), request.getAddress())) {
            throw new CustomRuntimeException(ExceptionCode.COURT_ALREADY_EXISTS);
        }

        Court court = new Court(
                owner,
                request.getName(),
                request.getAddress(),
                request.getCourtType(),
                request.getDescription()
        );

        // 이미지 업로드
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (int i = 0; i < request.getImages().size(); i++) {
                MultipartFile file = request.getImages().get(i);
                String imageUrl = s3Service.uploadFile(file, "courts");

                CourtImage courtImage = new CourtImage(
                        imageUrl,
                        i == 0 // 첫 번째 이미지를 썸네일로
                );
                court.addImage(courtImage);
            }
        }

        courtRepository.save(court);

        // CourtResponse 생성
        CourtResponse response = CourtResponse.from(court);

        // 1. 단건 캐시 저장
        redisTemplate.opsForValue().set(COURT_CACHE_PREFIX + court.getCourtId(), response, 10, TimeUnit.MINUTES);

        return response;
    }


    @Transactional(readOnly = true)
    public List<CourtResponse> getAllCourts() {
        String cacheKey = COURT_CACHE_PREFIX + "all";

        Object cachedObj = redisTemplate.opsForValue().get(cacheKey);
        if (cachedObj != null) {
            return (List<CourtResponse>) cachedObj;
        }

        // DB 조회
        List<CourtResponse> list = courtRepository.findAllWithImages().stream()
                .map(CourtResponse::from)
                .collect(Collectors.toList());

        // 캐시에 저장 (TTL 10분)
        redisTemplate.opsForValue().set(cacheKey, list, 10, TimeUnit.MINUTES);

        return list;
    }
    //value = "court": Redis 캐시 이름
    //key = "#courtId": courtId별로 캐싱
    //첫 조회 시 DB에서 가져와 Redis에 저장
    //이후 동일 courtId 요청 시 Redis에서 바로 반환함
//    @Transactional(readOnly = true)
//    @Cacheable(value = "court", key = "#courtId")
//    public CourtResponse getCourtById(Long courtId) {
//        Court court = courtRepository.findByIdWithImages(courtId);
//        if (court == null) throw new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND);
//        return CourtResponse.from(court);
//    }

    //동작 흐름
    //getCourtById
    //캐시 확인 → 있으면 즉시 반환
    //없으면 Redisson 분산락 획득
    //락을 잡은 상태에서 DB 조회 후 캐시에 저장 (TTL 10분)
    //다른 요청이 기다리는 동안 캐시가 채워지면 DB 중복 호출 방지
    //updateCourt / deleteCourt
    //@CacheEvict로 캐시 제거
    //다음 조회 때 다시 캐시 생성

    /**
     * 분산락 기반 Cache Aside 패턴
     */
    @Transactional(readOnly = true)
    public CourtResponse getCourtById(Long courtId) {
        String cacheKey = COURT_CACHE_PREFIX + courtId;

        // 1. 캐시 먼저 확인
        Object cachedObject = redisTemplate.opsForValue().get(cacheKey);
        if (cachedObject != null) {
            // Object를 CourtResponse로 변환
            return objectMapper.convertValue(cachedObject, CourtResponse.class);
        }

        // 2. 분산락 획득 시도
        RLock lock = redissonClient.getLock("lock:" + cacheKey);
        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {

                // 2-1. 락을 잡은 후 캐시 재확인
                cachedObject = redisTemplate.opsForValue().get(cacheKey);
                if (cachedObject != null) {
                    return objectMapper.convertValue(cachedObject, CourtResponse.class);
                }

                // 3. DB 조회
                Court court = courtRepository.findByIdWithImages(courtId);
                if (court == null) {
                    throw new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND);
                }
                CourtResponse response = CourtResponse.from(court);

                // 4. 캐시에 저장 (TTL 10분)
                redisTemplate.opsForValue().set(cacheKey, response, 10, TimeUnit.MINUTES);

                return response;
            } else {
                // 5. 락을 못 잡으면 캐시 재시도
                Thread.sleep(50);
                Object retryCached = redisTemplate.opsForValue().get(cacheKey);
                if (retryCached != null) {
                    return objectMapper.convertValue(retryCached, CourtResponse.class);
                } else {
                    throw new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    //조회 속도 UP: Redis 캐시에서 바로 반환
    //DB 부하 DOWN: 반복 조회 시 DB 호출 최소화
    //데이터 일관성: 수정/삭제 시 캐시 자동 제거
    @Transactional
    @CacheEvict(value = "court", key = "#courtId")
    public CourtResponse updateCourt(Long courtId, CourtUpdate request, List<MultipartFile> newImages) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

        if (!court.getOwner().getOwnerId().equals(request.getOwnerId())) {
            throw new CustomRuntimeException(ExceptionCode.UPDATE_COURT_OWNER_ONLY);
        }

        // 기존 이미지 삭제 (S3 + DB)
        if (!court.getImages().isEmpty()) {
            for (CourtImage img : court.getImages()) {
                s3Service.deleteFile(img.getImageUrl());
                courtImageRepository.delete(img);
            }
            court.getImages().clear();
        }

        // 새 이미지 업로드
        if (newImages != null && !newImages.isEmpty()) {
            for (int i = 0; i < newImages.size(); i++) {
                MultipartFile file = newImages.get(i);
                String imageUrl = s3Service.uploadFile(file, "courts");
                CourtImage courtImage = new CourtImage(court, imageUrl, i == 0);
                court.addImage(courtImage);
            }
        }

        court.updateCourt(
                request.getName(),
                request.getAddress(),
                request.getCourtType(),
                request.getDescription()
        );

        CourtResponse response = CourtResponse.from(court);

        // 업데이트 후 캐시 갱신
        redisTemplate.opsForValue().set(COURT_CACHE_PREFIX + courtId, response, 10, TimeUnit.MINUTES);

        // 전체 목록 캐시 삭제
        redisTemplate.delete(COURT_CACHE_PREFIX + "all");

        return response;
    }


    @Transactional
    @CacheEvict(value = "court", key = "#courtId")
    public void deleteCourt(Long courtId, Long ownerId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

        if (!court.getOwner().getOwnerId().equals(ownerId)) {
            throw new CustomRuntimeException(ExceptionCode.DELETE_COURT_OWNER_ONLY);
        }

        // 이미지 먼저 삭제 (S3 + DB)
        for (CourtImage img : court.getImages()) {
            s3Service.deleteFile(img.getImageUrl());
            courtImageRepository.delete(img);
        }

        courtRepository.delete(court);

        // 캐시 삭제
        redisTemplate.delete(COURT_CACHE_PREFIX + courtId);
        redisTemplate.delete(COURT_CACHE_PREFIX + "all");
    }

    @Transactional
    @CacheEvict(value = "court", key = "#courtId")
    public void deleteCourtImage(Long imageId, Long ownerId) {
        CourtImage image = courtImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.IMAGE_NOT_FOUND));

        Long imageOwnerId = image.getCourt().getOwner().getOwnerId();
        if (!imageOwnerId.equals(ownerId)) {
            throw new CustomRuntimeException(ExceptionCode.DELETE_IMAGE_OWNER_ONLY);
        }
        s3Service.deleteFile(image.getImageUrl());
        courtImageRepository.delete(image);
    }

    @Transactional
    public void uploadCourtImage(Long courtId, MultipartFile file) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

        String imageUrl = s3Service.uploadFile(file, "courts");

        CourtImage image = new CourtImage(imageUrl, false);
        court.addImage(image);

        courtRepository.save(court);
    }
}
