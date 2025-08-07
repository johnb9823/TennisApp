package com.example.tennisapp.domain.reservation.dto.response;

import com.example.tennisapp.domain.reservation.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ReservationListResponse {

    private final Long reservationId;
    private final String courtName; // 예약한 코트명
    private final LocalDate reservationDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final ReservationStatus status;
}
