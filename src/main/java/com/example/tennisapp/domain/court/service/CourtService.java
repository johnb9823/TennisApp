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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourtService {

    private final CourtRepository courtRepository;
    private final OwnerRepository ownerRepository;
    private final S3Service s3Service;
    private final CourtImageRepository courtImageRepository;

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

        courtRepository.save(court); // cascade 덕분에 images 자동 저장

        return new CourtResponse(
                court.getCourtId(),
                court.getName(),
                court.getAddress(),
                court.getCourtType(),
                court.getDescription(),
                court.getImages().stream().map(CourtImage::getImageUrl).toList(),
                court.getCreatedAt(),
                court.getUpdatedAt()
        );
    }


    @Transactional(readOnly = true)
    public List<CourtResponse> getAllCourts() {
        return courtRepository.findAll().stream()
                .map(court -> new CourtResponse(
                        court.getCourtId(),
                        court.getName(),
                        court.getAddress(),
                        court.getCourtType(),
                        court.getDescription(),
                        court.getImages().stream()
                                .map(image -> image.getImageUrl())
                                .collect(Collectors.toList()),
                        court.getCreatedAt(),
                        court.getUpdatedAt()
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourtResponse getCourtById(Long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

        return new CourtResponse(
                court.getCourtId(),
                court.getName(),
                court.getAddress(),
                court.getCourtType(),
                court.getDescription(),
                court.getImages().stream()
                        .map(image -> image.getImageUrl())
                        .collect(Collectors.toList()),
                court.getCreatedAt(),
                court.getUpdatedAt()
        );
    }

    @Transactional
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

        return CourtResponse.from(court);
    }


    @Transactional
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
    }

    @Transactional
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

        courtRepository.save(court); // cascade로 CourtImage도 저장됨
    }
}
