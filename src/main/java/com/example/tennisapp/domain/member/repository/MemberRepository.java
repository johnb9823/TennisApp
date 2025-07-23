package com.example.tennisapp.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	boolean existsByName(String name);

	Optional<Member> findByEmail(String email);

	Optional<Member> findByMemberIdAndIsDeletedFalse(Long memberId);

}
