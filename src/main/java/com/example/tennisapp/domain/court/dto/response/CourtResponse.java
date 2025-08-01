package com.example.tennisapp.domain.court.dto.response;

import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.domain.court.enums.CourtType;
import com.example.tennisapp.domain.courtImage.entity.CourtImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CourtResponse {

    private final Long courtId;
    private final String name;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final CourtType courtType;
    private final String description;
    private final List<String> imageUrls;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CourtResponse from(Court court) {
        List<String> imageUrls = court.getImages().stream()
                .map(CourtImage::getImageUrl)
                .toList();

        return new CourtResponse(
                court.getCourtId(),
                court.getName(),
                court.getAddress(),
                court.getLatitude(),
                court.getLongitude(),
                court.getCourtType(),
                court.getDescription(),
                imageUrls,
                court.getCreatedAt(),
                court.getUpdatedAt()
        );
    }

    public CourtResponse(Long courtId, String name, String address, Double latitude, Double longitude,
                         CourtType courtType, String description, List<String> imageUrls,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.courtId = courtId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courtType = courtType;
        this.description = description;
        this.imageUrls = imageUrls;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }




}
