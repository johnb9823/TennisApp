package com.example.tennisapp.domain.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tennisapp.domain.board.entity.Board;
import com.example.tennisapp.domain.board.repository.BoardRepository;
import com.example.tennisapp.domain.comment.dto.request.CommentCreate;
import com.example.tennisapp.domain.comment.dto.request.CommentDelete;
import com.example.tennisapp.domain.comment.dto.request.CommentUpdate;
import com.example.tennisapp.domain.comment.dto.response.CommentResponse;
import com.example.tennisapp.domain.comment.entity.Comment;
import com.example.tennisapp.domain.comment.repository.CommentRepository;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public CommentResponse createComment(Long boardId, CommentCreate request, Member loginMember) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BOARD_NOT_FOUND));

		Comment parentComment = null;
		if (request.getParentCommentId() != null) {
			parentComment = commentRepository.findById(request.getParentCommentId())
				.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COMMENT_NOT_FOUND));
		}

		Comment comment = new Comment(board, loginMember, request.getContent(), parentComment);
		Comment savedComment = commentRepository.save(comment);

		return new CommentResponse(
			savedComment.getCommentId(),
			savedComment.getContent(),
			savedComment.getMember().getName()
		);
	}


	@Transactional
	public CommentResponse updateComment(Long commentId, CommentUpdate request, Member loginMember) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COMMENT_NOT_FOUND));

		if (!comment.getMember().getMemberId().equals(loginMember.getMemberId())) {
			throw new CustomRuntimeException(ExceptionCode.UPDATE_COMMENT_WRITER_ONLY);
		}

		comment.updateContent(request.getContent());

		return new CommentResponse(comment.getCommentId(), comment.getContent(), comment.getMember().getName());
	}

	@Transactional
	public void deleteComment(Long commentId, CommentDelete request, Member loginMember) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.COMMENT_NOT_FOUND));

		if (!comment.getMember().getMemberId().equals(loginMember.getMemberId())) {
			throw new CustomRuntimeException(ExceptionCode.DELETE_COMMENT_WRITER_ONLY);
		}

		if (!passwordEncoder.matches(request.getPassword(), loginMember.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		commentRepository.delete(comment);
	}

	@Transactional(readOnly = true)
	public List<CommentResponse> getCommentsByBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BOARD_NOT_FOUND));

		return board.getComments().stream()
			.map(c -> new CommentResponse(c.getCommentId(), c.getContent(), c.getMember().getName()))
			.collect(Collectors.toList());
	}
}
