package com.example.tennisapp.domain.reservation.service;

import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.domain.court.repository.CourtRepository;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.member.repository.MemberRepository;
import com.example.tennisapp.domain.reservation.dto.request.ReservationCreate;
import com.example.tennisapp.domain.reservation.dto.response.ReservationResponse;
import com.example.tennisapp.domain.reservation.entity.Reservation;
import com.example.tennisapp.domain.reservation.repository.ReservationRepository;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final CourtRepository courtRepository;

    public ReservationResponse createReservation(ReservationCreate create, Member member, Long courtId) {

        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COURT_NOT_FOUND));

        // 예약 날짜가 현재 시간보다 이전인 경우 예외 처리
        if (create.getReservationDate().isBefore(java.time.LocalDate.now())) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_NOT_ALLOWED);
        }
        // 예약 시간의 시작과 끝이 올바르지 않은 경우 예외 처리
        if (create.getStartTime().isAfter(create.getEndTime())) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_NOT_ALLOWED);
        }

        // 예약이 겹치는지 확인
        boolean isOverlap = reservationRepository.existsByCourtAndReservationDateAndStartTimeLessThanAndEndTimeGreaterThan(
                court,
                create.getReservationDate(),
                create.getEndTime(),
                create.getStartTime()
        );

        if (isOverlap) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_ALREADY_EXISTS);
        }

        Reservation courtReservation = new Reservation(
                member,
                court,
                create.getReservationDate(),
                create.getStartTime(),
                create.getEndTime()
        );

        Reservation savedReservation = reservationRepository.save(courtReservation);

        return new ReservationResponse(
                savedReservation.getReservationId(),
                savedReservation.getMember().getName(),     // Member 엔티티의 name
                savedReservation.getCourt().getName(),      // Court 엔티티의 name
                savedReservation.getReservationDate(),
                savedReservation.getStartTime(),
                savedReservation.getEndTime(),
                savedReservation.getStatus(),
                savedReservation.getCreatedAt()
        );
    }

    // 예약 수정
    public ReservationResponse updateReservation(Long reservationId, ReservationCreate updateRequest, Member member) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.RESERVATION_NOT_FOUND));

        // 본인 예약인지 확인
        if (!reservation.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_UPDATE_WRITER_ONLY);
        }

        if (updateRequest.getReservationDate().isBefore(java.time.LocalDate.now())) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_NOT_ALLOWED);
        }
        if (updateRequest.getStartTime().isAfter(updateRequest.getEndTime())) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_NOT_ALLOWED);
        }

        boolean isOverlap = reservationRepository.existsByCourtAndReservationDateAndStartTimeLessThanAndEndTimeGreaterThan(
                reservation.getCourt(),
                updateRequest.getReservationDate(),
                updateRequest.getEndTime(),
                updateRequest.getStartTime()
        );
        if (isOverlap) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_ALREADY_EXISTS);
        }

        // 수정
        reservation.updateReservation(
                updateRequest.getReservationDate(),
                updateRequest.getStartTime(),
                updateRequest.getEndTime()
        );

        return toResponse(reservation);
    }

    // 예약 삭제
    public void deleteReservation(Long reservationId, Member member) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.RESERVATION_NOT_FOUND));

        if (!reservation.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_CANCEL_WRITER_ONLY);
        }
        reservationRepository.delete(reservation);
    }

    // 예약 단건 조회
    public ReservationResponse getReservation(Long reservationId, Member member) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.RESERVATION_NOT_FOUND));

        // 본인 예약만 확인 가능
        if (!reservation.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomRuntimeException(ExceptionCode.RESERVATION_FIND_WRITER_ONLY);
        }

        return toResponse(reservation);
    }

    // 예약 목록 조회 (본인 것만)
    public List<ReservationResponse> getMyReservations(Member member) {
        return reservationRepository.findAllByMember(member).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getMember().getName(),
                reservation.getCourt().getName(),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }

}
