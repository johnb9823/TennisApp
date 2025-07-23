package com.example.tennisapp.domain.owner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.owner.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

	boolean existsByEmail(String email);

	boolean existsByName(String name);

	Optional<Owner> findByEmail(String email);

	Optional<Owner> findByOwnerIdAndIsDeletedFalse(Long ownerId);
}
