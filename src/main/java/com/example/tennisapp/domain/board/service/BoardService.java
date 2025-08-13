package com.example.tennisapp.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tennisapp.domain.board.dto.request.CreateRequest;
import com.example.tennisapp.domain.board.dto.request.UpdateRequest;
import com.example.tennisapp.domain.board.dto.response.PagedResponse;
import com.example.tennisapp.domain.board.dto.response.PostResponse;
import com.example.tennisapp.domain.board.entity.Board;
import com.example.tennisapp.domain.board.enums.PostCategory;
import com.example.tennisapp.domain.board.repository.BoardRepository;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

	private final BoardRepository boardRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public PostResponse createBoard(CreateRequest request, Member member) {
		Board board = new Board(
			member,
			request.getTitle(),
			request.getContent(),
			request.getCategory()
		);

		boardRepository.save(board);

		return toPostResponse(board);
	}

	@Transactional
	public PostResponse updateBoard(Long boardId, UpdateRequest request, Member member) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BOARD_NOT_FOUND));

		if (!board.getMember().getMemberId().equals(member.getMemberId())) {
			throw new CustomRuntimeException(ExceptionCode.UPDATE_BOARD_WRITER_ONLY);
		}

		PostCategory category = PostCategory.valueOf(request.getCategory());

		board.update(request.getTitle(), request.getContent(), category);

		return toPostResponse(board);
	}

	@Transactional
	public void deleteBoard(Long boardId, Member member, String password) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BOARD_NOT_FOUND));

		if (!board.getMember().getMemberId().equals(member.getMemberId())) {
			throw new CustomRuntimeException(ExceptionCode.DELETE_BOARD_WRITER_ONLY);
		}

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		boardRepository.delete(board);
	}

	@Transactional
	public PostResponse getBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BOARD_NOT_FOUND));

		board.increaseViewCount();

		return new PostResponse(board);
	}

	// N+1 문제 제거: fetch join 사용
	public PagedResponse<PostResponse> getBoards(int page, int size) {
		int offset = page * size;

		List<Board> boards = boardRepository.findAllWithMemberPaged(offset, size);
		long totalElements = boardRepository.count();
		int totalPages = (int) Math.ceil((double) totalElements / size);

		List<PostResponse> postResponses = boards.stream()
				.map(this::toPostResponse)
				.collect(Collectors.toList());

		return new PagedResponse<>(
				postResponses,
				page + 1,
				size,
				totalElements,
				totalPages,
				(page + 1) == totalPages
		);
	}


	private PostResponse toPostResponse(Board board) {
		return new PostResponse(
			board.getBoardId(),
			board.getTitle(),
			board.getContent(),
			board.getMember().getName(),
			board.getCategory(),
			board.getCreatedAt(),
			board.getUpdatedAt()
		);
	}
}
