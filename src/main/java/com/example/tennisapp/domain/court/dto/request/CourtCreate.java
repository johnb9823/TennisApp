package com.example.tennisapp.domain.court.dto.request;

import com.example.tennisapp.domain.court.enums.CourtType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourtCreate {

    private final String name;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final CourtType courtType;
    private final String description;

}
