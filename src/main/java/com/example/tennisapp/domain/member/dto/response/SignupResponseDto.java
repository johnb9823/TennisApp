package com.example.tennisapp.domain.member.dto.response;

import com.example.tennisapp.domain.member.entity.Member;

import lombok.Getter;

@Getter
public class SignupResponseDto {

	private final Long id;
	private final String name;
	private final String email;

	public SignupResponseDto(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}
}
