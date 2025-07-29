package com.example.tennisapp.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreate {

	@NotBlank
	private String content;

	private Long parentCommentId; // 대댓글이면 부모 댓글 ID 전달, 일반 댓글이면 null
}

