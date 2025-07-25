package com.example.tennisapp.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreate {
	
	@NotBlank
	private String content;
}

