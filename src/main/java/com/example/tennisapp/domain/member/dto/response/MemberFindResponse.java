package com.example.tennisapp.domain.member.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import com.example.tennisapp.domain.member.entity.Member;

import lombok.Getter;

@Getter
public class MemberFindResponse {

	private final String name;
	private final int age;
	private final String experienceLevel;
	private final String region;

	public MemberFindResponse(Member member) {
		this.name = member.getName();
		this.age = calculateAge(member.getBirthdate());
		this.experienceLevel = member.getExperienceLevel().name();  // Enum → String
		this.region = member.getRegion().name();

	}

	private int calculateAge(LocalDate birthdate) {
		if (birthdate == null) return 0;
		return Period.between(birthdate, LocalDate.now()).getYears();
	}
}
