package com.example.tennisapp.domain.owner.dto.response;

import com.example.tennisapp.domain.owner.entity.Owner;
import lombok.Getter;

@Getter
public class LoginResponse {
	private Long id;
	private String name;
	private String email;

	public LoginResponse(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public static LoginResponse from(Owner owner) {
		return new LoginResponse(owner.getOwnerId(), owner.getEmail(), owner.getName());
	}
}
