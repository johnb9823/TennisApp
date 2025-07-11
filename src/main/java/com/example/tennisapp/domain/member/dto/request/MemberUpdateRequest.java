package com.example.tennisapp.domain.member.dto.request;

import lombok.Getter;

@Getter
public class MemberUpdateRequest {

	private final String name;

	private final String content;

	private final String experienceLevel;

	private final String region;

	public MemberUpdateRequest(String name, String content, String experienceLevel, String region) {
		this.name = name;
		this.content = content;
		this.experienceLevel = experienceLevel;
		this.region = region;
	}
}
