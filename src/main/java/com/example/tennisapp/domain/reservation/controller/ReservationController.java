package com.example.tennisapp.domain.reservation.controller;

import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.reservation.dto.request.ReservationCreate;
import com.example.tennisapp.domain.reservation.dto.response.ReservationResponse;
import com.example.tennisapp.domain.reservation.service.ReservationService;
import com.example.tennisapp.global.error.UnauthorizedException;
import com.example.tennisapp.global.success.ApiResponse;
import com.example.tennisapp.global.success.SuccessCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @RequestBody ReservationCreate request,
            HttpSession session
    ) {
        Member member = getSessionMember(session);
        ReservationResponse response = reservationService.createReservation(request, member, request.getCourtId());
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.RESERVATION_SUCCESS, response));
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationCreate request,
            HttpSession session
    ) {
        Member member = getSessionMember(session);
        ReservationResponse response = reservationService.updateReservation(reservationId, request, member);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.RESERVATION_UPDATED, response));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> deleteReservation(
            @PathVariable Long reservationId,
            HttpSession session
    ) {
        Member member = getSessionMember(session);
        reservationService.deleteReservation(reservationId, member);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.RESERVATION_CANCEL));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservation(
            @PathVariable Long reservationId,
            HttpSession session
    ) {
        Member member = getSessionMember(session);
        ReservationResponse response = reservationService.getReservation(reservationId, member);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.RESERVATION_RETRIEVED, response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getMyReservations(
            HttpSession session
    ) {
        Member member = getSessionMember(session);
        List<ReservationResponse> reservations = reservationService.getMyReservations(member);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.RESERVATION_RETRIEVED, reservations));
    }

    private Member getSessionMember(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("LOGIN_MEMBER");
        if (loginMember == null) throw new UnauthorizedException("로그인이 필요합니다.");
        return loginMember;
    }
}
