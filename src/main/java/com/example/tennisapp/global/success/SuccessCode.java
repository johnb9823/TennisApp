package com.example.tennisapp.global.success;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
	// 회원가입, 로그인
	SIGNUP_SUCCESS(HttpStatus.OK, "회원가입 완료"),
	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),

	// 회원
	FIND_MEMBER_SUCCESS(HttpStatus.OK, "회원 조회 성공"),
	UPDATE_MEMBER_SUCCESS(HttpStatus.OK, "회원정보 수정 성공"),
	UPDATE_PASSWORD_SUCCESS(HttpStatus.OK, "비밀번호 수정 성공"),
	DELETE_MEMBER_SUCCESS(HttpStatus.OK, "회원 탈퇴 완료"),

	// 게시판
	CREATE_BOARD_SUCCESS(HttpStatus.OK, "게시물 작성 성공"),
	UPDATE_BOARD_SUCCESS(HttpStatus.OK, "게시물 수정 성공"),
	GET_BOARD_SUCCESS(HttpStatus.OK, "게시물 조회 성공"),
	GET_ALL_BOARDS_SUCCESS(HttpStatus.OK, "게시물 전체 조회 성공"),
	DELETE_BOARD_SUCCESS(HttpStatus.OK, "게시물 삭제 성공"),

	//댓글
	CREATE_COMMENT_SUCCESS(HttpStatus.OK, "댓글 작성 성공"),
	UPDATE_COMMENT_SUCCESS(HttpStatus.OK, "댓글 수정 성공"),
	DELETE_COMMENT_SUCCESS(HttpStatus.OK, "삭제 삭제 성공"),
	GET_COMMENT_SUCCESS(HttpStatus.OK, "댓글 조회 성공"),

	// 신고
	REPORT_SUCCESS(HttpStatus.CREATED, "신고가 성공적으로 접수되었습니다."),

	//예약
	RESERVATION_SUCCESS(HttpStatus.CREATED, "예약이 완료되었습니다."),
	RESERVATION_RETRIEVED(HttpStatus.OK, "예약 조회가 완료되었습니다."),
	RESERVATION_UPDATED(HttpStatus.OK, "예약 정보 수정이 완료되었습니다."),
	RESERVATION_CANCEL(HttpStatus.OK, "예약이 취소되었습니다."),

	// 코트
	COURT_CREATED_SUCCESS(HttpStatus.CREATED, "코트 등록이 완료되었습니다."),
	COURT_UPDATED_SUCCESS(HttpStatus.OK, "코트 수정이 완료되었습니다."),
	COURT_DELETED_SUCCESS(HttpStatus.OK, "코트 삭제가 완료되었습니다."),
	GET_COURT_LIST_SUCCESS(HttpStatus.OK, "코트 목록 조회가 완료되었습니다."),
	GET_COURT_SUCCESS(HttpStatus.OK, "코트 조회가 완료되었습니다."),

	//정상작동
	SUCCESS_OK(HttpStatus.OK, "요청이 정상적으로 처리되었습니다."),
	DELETE_OK(HttpStatus.OK, "삭제가 정상적으로 처리되었습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
