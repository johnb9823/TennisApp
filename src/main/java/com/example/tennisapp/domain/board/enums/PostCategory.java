package com.example.tennisapp.domain.board.enums;

public enum PostCategory {

	FREE("자유 게시판"),
	REVIEW("후기 게시판"),
	QUESTION("질문 게시판"),
	AD("광고");

	private final String displayName;

	PostCategory(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
