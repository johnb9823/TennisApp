package com.example.tennisapp.domain.court.service;

import com.example.tennisapp.domain.court.dto.request.CourtCreate;
import com.example.tennisapp.domain.court.dto.request.CourtUpdate;
import com.example.tennisapp.domain.court.dto.response.CourtResponse;
import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.domain.court.repository.CourtRepository;
import com.example.tennisapp.domain.owner.entity.Owner;
import com.example.tennisapp.domain.owner.repository.OwnerRepository;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;
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
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.OWNER_NOT_FOUND));

        if (courtRepository.existsByNameAndAddress(request.getName(), request.getAddress())) {
            throw new CustomRuntimeException(ExceptionCode.COURT_ALREADY_EXISTS);
        }

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
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

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
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

        if (!court.getOwner().getOwnerId().equals(request.getOwnerId())) {
            throw new CustomRuntimeException(ExceptionCode.UPDATE_COURT_OWNER_ONLY);
        }

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
    public void deleteCourt(Long courtId, Long ownerId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

        if (!court.getOwner().getOwnerId().equals(ownerId)) {
            throw new CustomRuntimeException(ExceptionCode.DELETE_COURT_OWNER_ONLY);
        }
        courtRepository.delete(court);
    }
}
