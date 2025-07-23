package com.example.tennisapp.domain.member.entity;

import java.time.LocalDate;

import com.example.tennisapp.domain.member.enums.ExperienceLevel;
import com.example.tennisapp.domain.member.enums.Region;
import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Past(message = "생년월일은 과거 날짜여야 합니다.")
	@Column(nullable = false)
	private LocalDate birthdate;

	@Column(columnDefinition = "TEXT")
	private String content;  // 자기소개

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExperienceLevel experienceLevel; // 테니스 실력

	@Enumerated(EnumType.STRING)
	@Column(name = "region")
	private Region region; // 활동 지역

	@Column(nullable = false)
	private boolean isDeleted = false;   // 탈퇴 여부 (soft delete)



	public Member(String name, String email, String password, LocalDate birthdate,
		String content, ExperienceLevel experienceLevel, Region region) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthdate = birthdate;
		this.content = content;
		this.experienceLevel = experienceLevel;
		this.region = region;
	}


	public void softDelete() {
		this.isDeleted = true;
	}

	public void updateProfile(String name, String content, ExperienceLevel experienceLevel, Region region) {
		this.name = name;
		this.content = content;
		this.experienceLevel = experienceLevel;
		this.region = region;

	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

}

