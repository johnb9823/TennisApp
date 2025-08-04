package com.example.tennisapp.domain.court.service;

import com.example.tennisapp.domain.court.dto.request.CourtCreate;
import com.example.tennisapp.domain.court.dto.request.CourtUpdate;
import com.example.tennisapp.domain.court.dto.response.CourtResponse;
import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.domain.court.repository.CourtRepository;
import com.example.tennisapp.domain.owner.entity.Owner;
import com.example.tennisapp.domain.owner.repository.OwnerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourtService {

    private final CourtRepository courtRepository;
    private final OwnerRepository ownerRepository;

    @Transactional
    public CourtResponse createCourt(CourtCreate request) {
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 오너가 존재하지 않습니다."));
        Court court = new Court(
                owner,
                request.getName(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude(),
                request.getCourtType(),
                request.getDescription()
        );

        Court savedCourt = courtRepository.save(court);

        return new CourtResponse(
                savedCourt.getCourtId(),
                savedCourt.getName(),
                savedCourt.getAddress(),
                savedCourt.getLatitude(),
                savedCourt.getLongitude(),
                savedCourt.getCourtType(),
                savedCourt.getDescription(),
                null,
                savedCourt.getCreatedAt(),
                savedCourt.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<CourtResponse> getAllCourts() {
        return courtRepository.findAll().stream()
                .map(court -> new CourtResponse(
                        court.getCourtId(),
                        court.getName(),
                        court.getAddress(),
                        court.getLatitude(),
                        court.getLongitude(),
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
                .orElseThrow(() -> new IllegalArgumentException("해당 코트가 존재하지 않습니다."));

        return new CourtResponse(
                court.getCourtId(),
                court.getName(),
                court.getAddress(),
                court.getLatitude(),
                court.getLongitude(),
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
    public CourtResponse updateCourt(Long courtId, CourtUpdate request) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new IllegalArgumentException("해당 코트가 존재하지 않습니다."));

        court.updateCourt(
                request.getName(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude(),
                request.getCourtType(),
                request.getDescription()
        );

        return new CourtResponse(
                court.getCourtId(),
                court.getName(),
                court.getAddress(),
                court.getLatitude(),
                court.getLongitude(),
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
    public void deleteCourt(Long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new IllegalArgumentException("해당 코트가 존재하지 않습니다."));
        courtRepository.delete(court);
    }
}
