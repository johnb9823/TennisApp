package com.example.tennisapp.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tennisapp.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
