package com.example.tennisapp.domain.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tennisapp.domain.comment.dto.request.CommentCreate;
import com.example.tennisapp.domain.comment.dto.request.CommentDelete;
import com.example.tennisapp.domain.comment.dto.request.CommentUpdate;
import com.example.tennisapp.domain.comment.dto.response.CommentResponse;
import com.example.tennisapp.domain.comment.service.CommentService;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.global.error.UnauthorizedException;
import com.example.tennisapp.global.success.ApiResponse;
import com.example.tennisapp.global.success.SuccessCode;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<ApiResponse<CommentResponse>> createComment(
		@PathVariable Long boardId,
		@Valid @RequestBody CommentCreate request,
		HttpSession session
	)  {
		Member loginMember = getSessionMember(session);
		CommentResponse commentResponse = commentService.createComment(boardId ,request, loginMember);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.CREATE_COMMENT_SUCCESS, commentResponse));
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
		@PathVariable Long commentId,
		@Valid @RequestBody CommentUpdate request,
		HttpSession session
	)  {
		Member loginMember = getSessionMember(session);
		CommentResponse commentResponse = commentService.updateComment(commentId, request, loginMember);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.CREATE_COMMENT_SUCCESS, commentResponse));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ApiResponse<CommentDelete>> deleteComment(
		@PathVariable Long commentId,
		@Valid @RequestBody CommentDelete request,
		HttpSession session
	) {
		Member loginMember = getSessionMember(session);
		commentService.deleteComment(commentId, request, loginMember);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.DELETE_COMMENT_SUCCESS));
	}

	// 특정 게시글의 전체 댓글 조회
	@GetMapping
	public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByBoard(
		@PathVariable Long boardId
	) {
		List<CommentResponse> comments = commentService.getCommentsByBoard(boardId);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.GET_COMMENT_SUCCESS, comments));
	}


	private Member getSessionMember(HttpSession session) {
		Member loginMember = (Member) session.getAttribute("LOGIN_MEMBER");
		if (loginMember == null) throw new UnauthorizedException("로그인이 필요합니다.");
		return loginMember;
	}
}
