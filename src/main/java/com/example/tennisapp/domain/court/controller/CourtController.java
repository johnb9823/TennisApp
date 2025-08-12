package com.example.tennisapp.domain.court.controller;

import com.example.tennisapp.domain.court.dto.request.CourtCreate;
import com.example.tennisapp.domain.court.dto.request.CourtUpdate;
import com.example.tennisapp.domain.court.dto.response.CourtResponse;
import com.example.tennisapp.domain.court.service.CourtService;
import com.example.tennisapp.global.success.ApiResponse;
import com.example.tennisapp.global.success.SuccessCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courts")
public class CourtController {

    private final CourtService courtService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CourtResponse>> createCourt(@ModelAttribute CourtCreate request) {
        CourtResponse response = courtService.createCourt(request);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.COURT_CREATED_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourtResponse>>> getAllCourts() {
        List<CourtResponse> courts = courtService.getAllCourts();
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.GET_COURT_LIST_SUCCESS, courts));
    }

    @GetMapping("/{courtId}")
    public ResponseEntity<ApiResponse<CourtResponse>> getCourtById(@PathVariable Long courtId) {
        CourtResponse court = courtService.getCourtById(courtId);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.GET_COURT_SUCCESS, court));
    }

    @PutMapping(value = "/{courtId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CourtResponse>> updateCourt(
            @PathVariable Long courtId,
            @ModelAttribute CourtUpdate request,
            @RequestPart(required = false) List<MultipartFile> newImages
    ) {
        CourtResponse updated = courtService.updateCourt(courtId, request, newImages);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.COURT_UPDATED_SUCCESS, updated));
    }


    @DeleteMapping("/{courtId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourt(
            @PathVariable Long courtId,
            HttpSession session
    ) {
        Long ownerId = (Long) session.getAttribute("OWNER_ID");
        courtService.deleteCourt(courtId, ownerId);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.COURT_DELETED_SUCCESS, null));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourtImage(
            @PathVariable Long imageId,
            HttpSession session
    ) {
        Long ownerId = (Long) session.getAttribute("OWNER_ID");
        courtService.deleteCourtImage(imageId, ownerId);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.IMAGE_DELETED_SUCCESS, null));
    }

}
