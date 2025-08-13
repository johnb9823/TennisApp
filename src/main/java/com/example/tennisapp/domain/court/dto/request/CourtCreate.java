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
public class CourtCreate {

    private Long ownerId;
    private String name;
    private String address;
    private CourtType courtType;
    private String description;
    private List<MultipartFile> images;

//    // 컨트롤러에서 Owner ID를 주입할 때 사용
//    public static CourtCreate withOwnerId(Long ownerId, CourtCreate original) {
//        return new CourtCreate(
//                ownerId,
//                original.getName(),
//                original.getAddress(),
//                original.getCourtType(),
//                original.getDescription(),
//                original.getImages()
//        );
//    }

    public static CourtCreate withOwnerId(Long ownerId, CourtCreate request) {
        return CourtCreate.builder()
                .name(request.getName())
                .address(request.getAddress())
                .description(request.getDescription())
                .courtType(request.getCourtType())
                .images(request.getImages())
                .ownerId(ownerId)
                .build();
    }

}
