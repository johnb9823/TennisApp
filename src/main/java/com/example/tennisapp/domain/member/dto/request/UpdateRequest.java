package com.example.tennisapp.domain.member.dto.request;

import com.example.tennisapp.domain.member.enums.ExperienceLevel;
import com.example.tennisapp.domain.member.enums.Region;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRequest {

	@NotBlank
	private final String name;

	@NotNull
	private final String content;

	@NotNull
	private final ExperienceLevel experienceLevel;

	@NotNull
	private final Region region;

	public UpdateRequest(String name, String content, ExperienceLevel experienceLevel, Region region) {
		this.name = name;
		this.content = content;
		this.experienceLevel = experienceLevel;
		this.region = region;
	}
}
