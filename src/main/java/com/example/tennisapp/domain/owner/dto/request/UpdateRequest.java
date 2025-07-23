package com.example.tennisapp.domain.owner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRequest {

	@NotBlank
	private final String name;

	@NotNull
	private final String content;


	public UpdateRequest(String name, String content) {
		this.name = name;
		this.content = content;

	}
}
