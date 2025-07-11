package com.example.tennisapp.domain.member.dto.request;

import lombok.Getter;

@Getter
public class MemberDeleteRequest {

	private final String password;

	public MemberDeleteRequest(String password) {
		this.password = password;
	}
}
