package com.example.tennisapp.domain.courtImage.repository;

import com.example.tennisapp.domain.courtImage.entity.CourtImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtImageRepository extends JpaRepository<CourtImage, Long> {
}
