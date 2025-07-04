package com.example.tennisapp.domain.member.entity;

import java.time.LocalDate;

import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private LocalDate birthdate;

	@Column(columnDefinition = "TEXT")
	private String content;  // 자기소개

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExperienceLevel experienceLevel;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberStatus status;
}

