package com.example.tennisapp.domain.court.repository;

import com.example.tennisapp.domain.court.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court, Long> {
}
