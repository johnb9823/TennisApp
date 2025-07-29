package com.example.tennisapp.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.tennisapp.domain.comment.entity.Comment;

import lombok.Getter;

@Getter
public class CommentResponse {

	private Long commentId;

	private String content;

	private String writer;

	private List<CommentResponse> childComments;

	public CommentResponse(Long commentId, String content, String writer) {
		this.commentId = commentId;
		this.content = content;
		this.writer = writer;
	}

	public CommentResponse(Comment comment) {
		this.commentId = comment.getCommentId();
		this.content = comment.getContent();
		this.writer = comment.getMember().getName();
		this.childComments = comment.getChildComments().stream()
			.map(CommentResponse::new)
			.collect(Collectors.toList());
	}
}
