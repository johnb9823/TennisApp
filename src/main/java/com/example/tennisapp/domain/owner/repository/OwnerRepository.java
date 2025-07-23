package com.example.tennisapp.domain.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.owner.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
	boolean existsByEmail(String email);
}
