package com.example.tennisapp.domain.reservation.dto.response;

import com.example.tennisapp.domain.reservation.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ReservationResponse {

    private final Long reservationId;
    private final String memberName;   // Member 엔티티에서 가져옴
    private final String courtName;    // Court 엔티티에서 가져옴
    private final LocalDate reservationDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final ReservationStatus status;
    private final LocalDateTime createdAt; // BaseEntity에서

}
