package com.example.tennisapp.domain.owner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tennisapp.domain.owner.dto.request.DeleteRequest;
import com.example.tennisapp.domain.owner.dto.request.LoginRequest;
import com.example.tennisapp.domain.owner.dto.request.PasswordUpdateRequest;
import com.example.tennisapp.domain.owner.dto.request.SignupRequest;
import com.example.tennisapp.domain.owner.dto.request.UpdateRequest;
import com.example.tennisapp.domain.owner.dto.response.LoginResponse;
import com.example.tennisapp.domain.owner.dto.response.MyProfileResponse;
import com.example.tennisapp.domain.owner.dto.response.SignupResponseDto;
import com.example.tennisapp.domain.owner.entity.Owner;
import com.example.tennisapp.domain.owner.service.OwnerService;
import com.example.tennisapp.global.error.UnauthorizedException;
import com.example.tennisapp.global.success.ApiResponse;
import com.example.tennisapp.global.success.SuccessCode;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerController {

	private final OwnerService ownerService;

	//회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequest request) {
		ownerService.signup(request);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.SIGNUP_SUCCESS));
	}
	//로그인
//	@PostMapping("/login")
//	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request, HttpSession session) {
//		LoginResponse response = ownerService.login(request, session);
//		return ResponseEntity.ok(ApiResponse.of(SuccessCode.LOGIN_SUCCESS));
//	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request, HttpSession session) {
		LoginResponse loginResponse = ownerService.login(request, session);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.LOGIN_SUCCESS, loginResponse));
	}


	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
		ownerService.logout(session);
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.LOGOUT_SUCCESS));
	}

	// 내프로필 조회
	@GetMapping("/me/profile")
	public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile(HttpSession session) {
		Owner loginOwner = getSessionOwner(session);
		MyProfileResponse response = ownerService.getMyProfile(loginOwner.getOwnerId());
		return ResponseEntity.ok(ApiResponse.of(SuccessCode.FIND_MEMBER_SUCCESS, response));
	}

	// 내 프로필 수정
	@PutMapping("/me/profile")
	public ResponseEntity<ApiResponse<Void>> updateMyProfile(
		@Valid @RequestBody UpdateRequest request,
		HttpSession session
	) {
		Owner loginOwner = getSessionOwner(session);
		Owner updatedOwner = ownerService.updateProfile(loginOwner.getOwnerId(), request);

		// 세션 최신화
		session.setAttribute("loginOwner", updatedOwner);

		return ResponseEntity.ok(
			ApiResponse.of(SuccessCode.UPDATE_MEMBER_SUCCESS, null)
		);
	}

	// 비밀번호 수정
	@PatchMapping("/me/password")
	public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody PasswordUpdateRequest request, HttpSession session) {
		Owner loginOwner = getSessionOwner(session);
		ownerService.updatePassword(loginOwner.getOwnerId(), request);
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
		Owner loginOwner = getSessionOwner(session);
		ownerService.softDeleteOwner(loginOwner.getOwnerId(), request);
		session.invalidate(); // 세션 만료

		return ResponseEntity.ok(
			ApiResponse.of(SuccessCode.DELETE_MEMBER_SUCCESS, null)
		);
	}

	private Owner getSessionOwner(HttpSession session) {
		Owner loginOwner = (Owner) session.getAttribute("LOGIN_OWNER");
		if (loginOwner == null) throw new UnauthorizedException("로그인이 필요합니다.");
		return loginOwner;
	}
}
