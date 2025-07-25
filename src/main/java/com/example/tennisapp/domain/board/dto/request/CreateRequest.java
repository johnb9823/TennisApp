package com.example.tennisapp.domain.board.dto.request;

import com.example.tennisapp.domain.board.enums.PostCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRequest {

	@NotBlank(message = "제목은 필수입니다.")
	private final String title;

	@NotBlank(message = "내용은 필수입니다.")
	private final String content;

	@NotNull
	private final PostCategory category; // 예: 자유게시판, 질문, 후기 등
}
