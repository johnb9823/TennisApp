package com.example.tennisapp.domain.comment.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CommentResponse {

	private Long commentId;

	private String content;

	private String writer;

	public CommentResponse(Long commentId, String content, String writer) {
		this.commentId = commentId;
		this.content = content;
		this.writer = writer;
	}
}
