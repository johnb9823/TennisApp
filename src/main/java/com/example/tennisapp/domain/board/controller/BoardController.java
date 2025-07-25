package com.example.tennisapp.domain.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tennisapp.domain.board.dto.request.CreateRequest;
import com.example.tennisapp.domain.board.dto.request.DeleteRequest;
import com.example.tennisapp.domain.board.dto.request.UpdateRequest;
import com.example.tennisapp.domain.board.dto.response.PagedResponse;
import com.example.tennisapp.domain.board.dto.response.PostResponse;
import com.example.tennisapp.domain.board.entity.Board;
import com.example.tennisapp.domain.board.service.BoardService;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.global.error.UnauthorizedException;
import com.example.tennisapp.global.success.ApiResponse;
import com.example.tennisapp.global.success.SuccessCode;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

	private final BoardService boardService;

	/**
	 * 게시글 생성
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<PostResponse>> createBoard(
		@Valid @RequestBody CreateRequest request,
		HttpSession session) {
		Member loginMember = getSessionMember(session);
		PostResponse postResponse = boardService.createBoard(request, loginMember);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.CREATE_BOARD_SUCCESS, postResponse));
	}

	/**
	 * 게시글 수정
	 */
	@PutMapping("/{boardId}")
	public ResponseEntity<ApiResponse<PostResponse>> updateBoard(
		@PathVariable Long boardId,
		@Valid @RequestBody UpdateRequest request,
		HttpSession session) {
		Member loginMember = getSessionMember(session);
		PostResponse postResponse = boardService.updateBoard(boardId, request, loginMember);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.UPDATE_BOARD_SUCCESS, postResponse));
	}

	/**
	 * 게시글 삭제
	 */
	@DeleteMapping("/{boardId}")
	public ResponseEntity<ApiResponse<DeleteRequest>> deleteBoard(
		@PathVariable Long boardId,
		@Valid @RequestBody DeleteRequest request,
		HttpSession session) {
		Member loginMember = getSessionMember(session);
		boardService.deleteBoard(boardId, loginMember, request.getPassword());
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.DELETE_BOARD_SUCCESS, null));
	}

	/**
	 * 게시글 단건 조회
	 */
	@GetMapping("/{boardId}")
	public ResponseEntity<ApiResponse<PostResponse>> getBoard(@PathVariable Long boardId) {
		PostResponse postResponse = boardService.getBoard(boardId);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.GET_BOARD_SUCCESS, postResponse));
	}

	/**
	 * 게시글 목록 조회 (페이징)
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<PagedResponse<PostResponse>>> getBoards(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size) {
		PagedResponse<PostResponse> pagedResponse = boardService.getBoards(page-1, size);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.GET_ALL_BOARDS_SUCCESS, pagedResponse));
	}

	private Member getSessionMember(HttpSession session) {
		Member loginMember = (Member) session.getAttribute("LOGIN_MEMBER");
		if (loginMember == null) throw new UnauthorizedException("로그인이 필요합니다.");
		return loginMember;
	}
}



