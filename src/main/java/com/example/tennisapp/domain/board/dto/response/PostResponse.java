package com.example.tennisapp.domain.board.dto.response;

import java.time.LocalDateTime;

import com.example.tennisapp.domain.board.entity.Board;
import com.example.tennisapp.domain.board.enums.PostCategory;

import lombok.Getter;

@Getter
public class PostResponse {

	private final Long boardId;
	private final String title;
	private final String content;
	private final String writer;
	private final PostCategory category;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public PostResponse(Long boardId, String title, String content, String writer, PostCategory category,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.category = category;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public PostResponse(Board board) {
		this.boardId = board.getBoardId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.writer = board.getMember().getName();
		this.category = board.getCategory();
		this.createdAt = board.getCreatedAt();
		this.updatedAt = board.getUpdatedAt();
	}
}
