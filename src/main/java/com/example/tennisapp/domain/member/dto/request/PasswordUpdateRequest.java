package com.example.tennisapp.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordUpdateRequest {

	@NotBlank(message = "현재 비밀번호는 필수입니다.")
	private String oldPassword;

	@NotBlank(message = "새 비밀번호는 필수입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
	private String newPassword;

	public PasswordUpdateRequest(String oldPassword, String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
}
