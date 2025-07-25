package com.example.tennisapp.domain.board.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public final class PagedResponse<T> {

	private final List<T> content;    // 데이터 리스트

	private final int page;           // 현재 페이지 번호 (0부터 시작)

	private final int size;           // 페이지 크기 (한 페이지에 담기는 항목 수)

	private final long totalElements; // 전체 데이터 수

	private final int totalPages;     // 전체 페이지 수

	private final boolean last;       // 마지막 페이지 여부

	public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
		this.content = content;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.last = last;
	}
}
