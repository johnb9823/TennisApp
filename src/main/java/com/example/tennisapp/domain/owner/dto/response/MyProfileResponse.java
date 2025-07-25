package com.example.tennisapp.domain.owner.dto.response;

import java.time.LocalDate;
import java.time.Period;

import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.owner.entity.Owner;

import lombok.Getter;

@Getter
public class MyProfileResponse {

	private final String name;
	private final int age;
	private final String content;


	public MyProfileResponse(Owner owner) {
		this.name = owner.getName();
		this.age = calculateAge(owner.getBirthdate());
		this.content = owner.getContent();

	}

	private int calculateAge(LocalDate birthdate) {
		if (birthdate == null) return 0;
		return Period.between(birthdate, LocalDate.now()).getYears();
	}
}
