package com.example.tennisapp.domain.member.dto.request;

import java.time.LocalDate;

import com.example.tennisapp.domain.member.enums.ExperienceLevel;
import com.example.tennisapp.domain.member.enums.Region;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

	@NotBlank(message = "이름을 입력해주세요.")
	private final String name;

	@Email
	@NotBlank(message = "이메일을 입력해주세요.")
	private final String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
	private final String password;

	@NotNull(message = "생일을 입력해주세요.")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private final LocalDate birthdate;

	@NotNull(message = "자기소개를 입력해주세요.")
	private final String content;

	@NotNull(message = "테니스 실력을 입력해주세요.")
	private final ExperienceLevel experienceLevel;

	@NotNull(message = "활동 지역을 입력해주세요.")
	private final Region region;

	public SignupRequest(String name, String email, String password, LocalDate birthdate, String content,
		ExperienceLevel experienceLevel, Region region) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthdate = birthdate;
		this.content = content;
		this.experienceLevel = experienceLevel;
		this.region = region;
	}
}
