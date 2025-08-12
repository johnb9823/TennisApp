package com.example.tennisapp.domain.court.dto.request;

import com.example.tennisapp.domain.court.enums.CourtType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class CourtUpdate {

    private final Long ownerId;
    private final String name;
    private final String address;
    private final CourtType courtType;
    private final String description;
    private List<MultipartFile> newImages;

}
