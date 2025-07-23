package com.example.tennisapp.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tennisapp.domain.member.dto.request.DeleteRequest;
import com.example.tennisapp.domain.member.dto.request.LoginRequest;
import com.example.tennisapp.domain.member.dto.request.PasswordUpdateRequest;
import com.example.tennisapp.domain.member.dto.request.SignupRequest;
import com.example.tennisapp.domain.member.dto.request.UpdateRequest;
import com.example.tennisapp.domain.member.dto.response.LoginResponse;
import com.example.tennisapp.domain.member.dto.response.MemberProfileResponse;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.member.service.MemberService;
import com.example.tennisapp.global.error.UnauthorizedException;
import com.example.tennisapp.global.success.ApiResponse;
import com.example.tennisapp.global.success.SuccessCode;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	//회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request) {
		memberService.signup(request);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.SIGNUP_SUCCESS));
	}
	//로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request, HttpSession session) {
		LoginResponse response = memberService.login(request, session);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.LOGIN_SUCCESS));
	}

	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
		memberService.logout(session);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.LOGOUT_SUCCESS));
	}

	// 나의 프로필 조회
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MemberProfileResponse>> getMyProfile(HttpSession session) {
		Member loginMember = getSessionMember(session);
		return ResponseEntity.ok(
			ApiResponse.of(SuccessCode.FIND_MEMBER_SUCCESS, memberService.getProfile(loginMember.getMemberId())));
	}

	// 다른 유저 프로필 조회
	@GetMapping("/{memberId}")
	public ResponseEntity<ApiResponse<MemberProfileResponse>> getOtherProfile(@PathVariable Long memberId) {
		return ResponseEntity.ok(
			ApiResponse.of(SuccessCode.FIND_MEMBER_SUCCESS, memberService.getProfile(memberId))
		);
	}

	// 내 프로필 수정
	@PutMapping("/me/profile")
	public ResponseEntity<ApiResponse<Void>> updateMyProfile(
		@Valid @RequestBody UpdateRequest request,
		HttpSession session
	) {
		Member loginMember = getSessionMember(session);
		Member updatedMember = memberService.updateProfile(loginMember.getMemberId(), request);

		// 세션 최신화
		session.setAttribute("loginMember", updatedMember);

		return ResponseEntity.ok(
			ApiResponse.of(SuccessCode.UPDATE_MEMBER_SUCCESS, null)
		);
	}


	// 비밀번호 수정
	@PatchMapping("/me/password")
	public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody PasswordUpdateRequest request, HttpSession session) {
		Member loginMember = getSessionMember(session);
		memberService.updatePassword(loginMember.getMemberId(), request);
		return ResponseEntity.ok(
			ApiResponse.of(SuccessCode.UPDATE_PASSWORD_SUCCESS, null)
		);
	}

	// 회원 탈퇴
	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMyAccount(
		@RequestBody @Valid DeleteRequest request,
		HttpSession session
	) {
		Member loginMember = getSessionMember(session);
		memberService.softDeleteMember(loginMember.getMemberId(), request);
		session.invalidate(); // 세션 만료

		return ResponseEntity.ok(
			ApiResponse.of(SuccessCode.DELETE_MEMBER_SUCCESS, null)
		);
	}


	private Member getSessionMember(HttpSession session) {
		Member loginMember = (Member) session.getAttribute("LOGIN_MEMBER");
		if (loginMember == null) throw new UnauthorizedException("로그인이 필요합니다.");
		return loginMember;
	}


}
