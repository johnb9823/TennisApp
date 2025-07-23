package com.example.tennisapp.domain.member.dto.request;

import lombok.Getter;

@Getter
public class DeleteRequest {

	private final String password;

	public DeleteRequest(String password) {
		this.password = password;
	}
}
