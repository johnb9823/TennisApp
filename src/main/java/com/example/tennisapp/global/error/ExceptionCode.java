package com.example.tennisapp.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum ExceptionCode implements ErrorCode {

	VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "VALID_EXCEPTION가 발생했습니다, 로그를 확인해주세요"),

	// auth
	EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "중복된 이메일입니다."),
	NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "중복된 닉네임입니다."),
	USER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "지원하지 않는 사용자 역할입니다."),
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 혹은 비밀번호가 올바르지 않습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	INVALID_BIRTHDAY_IN_FUTURE(HttpStatus.BAD_REQUEST, "생일은 미래 날짜로 설정할 수 없습니다."),

	//코트
	COURT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 코트를 찾을 수 없습니다."),
	CANT_FIND_INTERFACE(HttpStatus.NOT_FOUND, "해당 정보를 찾을 수 없습니다"),
	KAKAO_API_EMPTY_RESULT(HttpStatus.NOT_FOUND, "Kakao API 응답에 좌표 정보가 없습니다."),

	//유저(=멤버)
	USER_CANT_FIND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다"),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
	SAME_PASSWORD(HttpStatus.BAD_REQUEST, "이전과 동일한 비밀번호는 사용이 불가합니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "입력한 비밀번호가 일치하지 않습니다."),
	SAME_NAME(HttpStatus.BAD_REQUEST, "이전과 동일한 이름은 변경할 수 없습니다."),
	SAME_CONTENT(HttpStatus.BAD_REQUEST, "이전과 동일한 소개입니다."),
	INVALID_EXPERIENCE_LEVEL(HttpStatus.BAD_REQUEST, "잘못된 경험 수준입니다."),
	INVALID_REGION(HttpStatus.BAD_REQUEST, "잘못된 지역 정보입니다."),

	// 신고
	ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고한 사용자입니다."),
	CANNOT_REPORT_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 신고할 수 없습니다."),
	REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "신고 내역이 존재하지 않습니다."),

	//리뷰
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
	STORE_REVIEW_MISMATCH(HttpStatus.BAD_REQUEST, "해당 가게에 속한 리뷰가 아닙니다."),
	DELETE_REVIEW_WRITER_ONLY(HttpStatus.BAD_REQUEST, "리뷰를 작성한 사용자만 삭제할 수 있습니다."),
	EDIT_REVIEW_WRITER_ONLY(HttpStatus.BAD_REQUEST, "리뷰를 작성한 사용자만 수정할 수 있습니다."),
	MEMBER_NOT_ELIGIBLE_FOR_REVIEW(HttpStatus.FORBIDDEN, "해당 가게를 이용한 사용자만 리뷰를 작성할 수 있습니다."),

	// 이미지
	S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 업로드에 실패했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
