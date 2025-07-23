package com.example.tennisapp.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tennisapp.domain.member.dto.request.DeleteRequest;
import com.example.tennisapp.domain.member.dto.request.LoginRequest;
import com.example.tennisapp.domain.member.dto.request.PasswordUpdateRequest;
import com.example.tennisapp.domain.member.dto.request.SignupRequest;
import com.example.tennisapp.domain.member.dto.request.UpdateRequest;
import com.example.tennisapp.domain.member.dto.response.LoginResponse;
import com.example.tennisapp.domain.member.dto.response.MemberProfileResponse;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.member.repository.MemberRepository;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(SignupRequest request) {
		if (memberRepository.existsByEmail(request.getEmail())) {
			throw new CustomRuntimeException(ExceptionCode.EMAIL_ALREADY_EXIST);
		}

		if (memberRepository.existsByName(request.getName())) {
			throw new CustomRuntimeException(ExceptionCode.NAME_ALREADY_EXIST);
		}

		Member member = new Member(
			request.getName(),
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getBirthdate(),
			request.getContent(),
			request.getExperienceLevel(),
			request.getRegion()
		);

		memberRepository.save(member);
	}

	public LoginResponse login(LoginRequest request, HttpSession session) {
		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.LOGIN_FAILED));

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		// 세션에 회원 정보 저장
		session.setAttribute("LOGIN_MEMBER", member);

		return new LoginResponse(member.getMemberId(), member.getName(), member.getEmail());
	}

	public void logout(HttpSession session) {
		session.invalidate(); // 세션 초기화
	}

	public MemberProfileResponse getProfile(Long memberId) {
		Member member = findById(memberId);
		return new MemberProfileResponse(member);
	}

	@Transactional
	public Member updateProfile(Long memberId, UpdateRequest request) {
		Member member = findById(memberId);
		member.updateProfile(
			request.getName(),
			request.getContent(),
			request.getExperienceLevel(),
			request.getRegion()
		);
		return member; // 컨트롤러에서 세션 갱신할 때 쓰기 위함
	}

	@Transactional
	public void updatePassword(Long memberId, PasswordUpdateRequest request) {
		Member member = findById(memberId);

		if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		if (passwordEncoder.matches(request.getNewPassword(), member.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.SAME_PASSWORD);
		}

		member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
	}

	@Transactional
	public void softDeleteMember(Long memberId, DeleteRequest request) {
		Member member = memberRepository.findByMemberIdAndIsDeletedFalse(memberId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		member.softDelete();
	}


	private Member findById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.MEMBER_NOT_FOUND));
	}

}
