package com.example.tennisapp.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum ExceptionCode implements ErrorCode {

	VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "VALID_EXCEPTION가 발생했습니다, 로그를 확인해주세요"),

	//유저
	EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "중복된 이메일입니다."),
	NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "중복된 닉네임입니다."),
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 혹은 비밀번호가 올바르지 않습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사장님입니다."),
	SAME_PASSWORD(HttpStatus.BAD_REQUEST, "이전과 동일한 비밀번호는 사용이 불가합니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

	//코트
	COURT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 코트를 찾을 수 없습니다."),
	COURT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 코트입니다."),
	UPDATE_COURT_OWNER_ONLY(HttpStatus.FORBIDDEN, "코트 사장님만 수정할 수 있습니다."),
	DELETE_COURT_OWNER_ONLY(HttpStatus.FORBIDDEN, "코트 사장님만 삭제할 수 있습니다."),
	CANT_FIND_INTERFACE(HttpStatus.NOT_FOUND, "해당 정보를 찾을 수 없습니다"),
	KAKAO_API_EMPTY_RESULT(HttpStatus.NOT_FOUND, "Kakao API 응답에 좌표 정보가 없습니다."),

	// 예약
	RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
	RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 예약된 시간입니다."),
	RESERVATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "예약이 불가능한 시간입니다."),
	RESERVATION_UPDATE_WRITER_ONLY(HttpStatus.FORBIDDEN, "예약 작성자만 수정할 수 있습니다."),
	RESERVATION_CANCEL_WRITER_ONLY(HttpStatus.FORBIDDEN, "예약 작성자만 취소할 수 있습니다."),
	RESERVATION_FIND_WRITER_ONLY(HttpStatus.FORBIDDEN, "예약 작성자만 취소할 수 있습니다."),

	//게시판
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
	UPDATE_BOARD_WRITER_ONLY(HttpStatus.NOT_FOUND, "게시글 작성자만 수정 수 있습니다."),
	DELETE_BOARD_WRITER_ONLY(HttpStatus.NOT_FOUND, "게시글 작성자만 삭제할 수 있습니다."),

	// 댓글
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
	UPDATE_COMMENT_WRITER_ONLY(HttpStatus.NOT_FOUND, "게시글 작성자만 수정 수 있습니다."),
	DELETE_COMMENT_WRITER_ONLY(HttpStatus.NOT_FOUND, "게시글 작성자만 삭제할 수 있습니다."),

	// 신고
	ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고한 사용자입니다."),
	CANNOT_REPORT_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 신고할 수 없습니다."),

	// 이미지
	IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "이미지를 찾을 수 없습니다."),
	DELETE_IMAGE_OWNER_ONLY(HttpStatus.FORBIDDEN, "이미지 소유자만 삭제할 수 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
