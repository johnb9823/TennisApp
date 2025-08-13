package com.example.tennisapp.domain.court.repository;

import com.example.tennisapp.domain.court.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourtRepository extends JpaRepository<Court, Long> {

    boolean existsByNameAndAddress(String name, String address);

    @Query("SELECT c FROM Court c LEFT JOIN FETCH c.images")
    List<Court> findAllWithImages();

    @Query("SELECT c FROM Court c LEFT JOIN FETCH c.images WHERE c.courtId = :courtId")
    Court findByIdWithImages(Long courtId);
}
