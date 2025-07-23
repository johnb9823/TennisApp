// package com.example.tennisapp.domain.owner.service;
//
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
//
// import com.example.tennisapp.domain.owner.entity.Owner;
// import com.example.tennisapp.domain.owner.repository.OwnerRepository;
//
// import lombok.Getter;
// import lombok.RequiredArgsConstructor;
//
// @Service
// @Getter
// @RequiredArgsConstructor
// public class AuthOwnerService {
//
// 	private final OwnerRepository ownerRepository;
// 	private final PasswordEncoder passwordEncoder;
//
// 	public void signup(SignupRequestDto request) {
// 		String encodedPassword = passwordEncoder.encode(request.getPassword());
//
// 		Owner owner = new Owner(
// 			request.getName(),
// 			request.getEmail(),
// 			encodedPassword,
// 			request.getBirthdate()
// 		);
//
// 		ownerRepository.save(owner);
// 	}
// }
