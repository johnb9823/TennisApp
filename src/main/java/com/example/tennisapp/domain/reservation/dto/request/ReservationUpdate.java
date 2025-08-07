package com.example.tennisapp.domain.reservation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ReservationUpdate {

    @NotNull
    @Future(message = "예약 날짜는 현재 날짜 이후여야 합니다.")
    private final LocalDate reservationDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
}
