package com.example.tennisapp.domain.owner.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tennisapp.domain.member.repository.MemberRepository;
import com.example.tennisapp.domain.owner.dto.request.DeleteRequest;
import com.example.tennisapp.domain.owner.dto.request.LoginRequest;
import com.example.tennisapp.domain.owner.dto.request.PasswordUpdateRequest;
import com.example.tennisapp.domain.owner.dto.request.SignupRequest;
import com.example.tennisapp.domain.owner.dto.request.UpdateRequest;
import com.example.tennisapp.domain.owner.dto.response.LoginResponse;
import com.example.tennisapp.domain.owner.dto.response.MyProfileResponse;
import com.example.tennisapp.domain.owner.entity.Owner;
import com.example.tennisapp.domain.owner.repository.OwnerRepository;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Getter
@RequiredArgsConstructor
public class OwnerService {

	private final OwnerRepository ownerRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	public void signup(SignupRequest request) {

		if (ownerRepository.existsByEmail(request.getEmail())) {
			throw new CustomRuntimeException(ExceptionCode.EMAIL_ALREADY_EXIST);
		}

		if (ownerRepository.existsByName(request.getName())) {
			throw new CustomRuntimeException(ExceptionCode.NAME_ALREADY_EXIST);
		}

		if (memberRepository.existsByEmail(request.getEmail())) {
			throw new CustomRuntimeException(ExceptionCode.EMAIL_ALREADY_EXIST);
		}

		if (memberRepository.existsByName(request.getName())) {
			throw new CustomRuntimeException(ExceptionCode.NAME_ALREADY_EXIST);
		}

		Owner owner = new Owner(
			request.getName(),
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getBirthdate(),
			request.getContent()
		);

		ownerRepository.save(owner);
	}

	public LoginResponse login(LoginRequest request, HttpSession session) {
		Owner owner = ownerRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.LOGIN_FAILED));

		if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		// 세션에 회원 정보 저장
		session.setAttribute("LOGIN_OWNER", owner);

		return new LoginResponse(owner.getOwnerId(), owner.getName(), owner.getEmail());
	}

	public void logout(HttpSession session) {
		session.invalidate(); // 세션 초기화
	}


	public MyProfileResponse getMyProfile(Long ownerId) {
		Owner owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.OWNER_NOT_FOUND));
		return new MyProfileResponse(owner);
	}


	@Transactional
	public Owner updateProfile(Long ownerId, UpdateRequest request) {
		Owner owner = findById(ownerId);
		owner.updateProfile(
			request.getName(),
			request.getContent()
		);
		return owner; // 컨트롤러에서 세션 갱신할 때 쓰기 위함
	}

	@Transactional
	public void updatePassword(Long ownerId, PasswordUpdateRequest request) {
		Owner owner = findById(ownerId);

		if (!passwordEncoder.matches(request.getOldPassword(), owner.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		if (passwordEncoder.matches(request.getNewPassword(), owner.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.SAME_PASSWORD);
		}

		owner.updatePassword(passwordEncoder.encode(request.getNewPassword()));
	}

	@Transactional
	public void softDeleteOwner(Long memberId, DeleteRequest request) {
		Owner owner = ownerRepository.findByOwnerIdAndIsDeletedFalse(memberId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
			throw new CustomRuntimeException(ExceptionCode.PASSWORD_MISMATCH);
		}

		owner.softDelete();
	}


	private Owner findById(Long ownerId) {
		return ownerRepository.findById(ownerId)
			.orElseThrow(() -> new CustomRuntimeException(ExceptionCode.MEMBER_NOT_FOUND));
	}
}
