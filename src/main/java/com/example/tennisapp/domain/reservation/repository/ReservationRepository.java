package com.example.tennisapp.domain.reservation.repository;

import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.reservation.entity.Reservation;
import com.example.tennisapp.domain.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByMember(Member member);

    // 특정 코트, 특정 날짜에, 예약시간이 겹치는 예약이 있는지 체크
    boolean existsByCourtAndReservationDateAndStartTimeLessThanAndEndTimeGreaterThan(
            Court court,
            LocalDate reservationDate,
            LocalTime endTime,
            LocalTime startTime
    );
}
