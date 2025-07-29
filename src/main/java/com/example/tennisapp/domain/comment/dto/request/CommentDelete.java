package com.example.tennisapp.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentDelete {

	@NotBlank(message = "비밀번호는 필수입니다.")
	private String password;

	public CommentDelete( String password) {
		this.password = password;
	}
}
