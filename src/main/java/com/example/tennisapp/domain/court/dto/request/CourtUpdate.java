package com.example.tennisapp.domain.court.dto.request;

import com.example.tennisapp.domain.court.enums.CourtType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CourtUpdate {

    private final Long ownerId;
    private final String name;
    private final String address;
    private final CourtType courtType;
    private final String description;
    private List<MultipartFile> newImages;

//    // 컨트롤러에서 Owner ID를 주입할 때 사용
//    public static CourtUpdate withOwnerId(Long ownerId, CourtUpdate original) {
//        return new CourtUpdate(
//                ownerId,
//                original.getName(),
//                original.getAddress(),
//                original.getCourtType(),
//                original.getDescription(),
//                original.getNewImages()
//        );
//    }

    public static CourtUpdate withOwnerId(Long ownerId, CourtUpdate request) {
        return CourtUpdate.builder()
                .name(request.getName())
                .address(request.getAddress())
                .description(request.getDescription())
                .courtType(request.getCourtType())
                .newImages(request.getNewImages())
                .ownerId(ownerId)
                .build();
    }

}
