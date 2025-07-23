package com.example.tennisapp.domain.member.dto.response;

import java.time.LocalDate;
import java.time.Period;

import com.example.tennisapp.domain.member.entity.Member;

import lombok.Getter;

@Getter
public class MemberProfileResponse {

	private final String name;
	private final int age;
	private final String content;
	private final String experienceLevel;
	private final String region;

	public MemberProfileResponse(Member member) {
		this.name = member.getName();
		this.age = calculateAge(member.getBirthdate());
		this.content = member.getContent();
		this.experienceLevel = member.getExperienceLevel().name();
		this.region = member.getRegion().name();
	}

	private int calculateAge(LocalDate birthdate) {
		if (birthdate == null) return 0;
		return Period.between(birthdate, LocalDate.now()).getYears();
	}
}
