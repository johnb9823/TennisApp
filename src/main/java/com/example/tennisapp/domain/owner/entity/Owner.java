package com.example.tennisapp.domain.owner.entity;

import java.time.LocalDate;

import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Owner extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ownerId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private LocalDate birthdate;

	@Column(nullable = false)
	private boolean status;  // true: 탈퇴한 상태, false: 정상 상태

	public Owner(String name, String email, String password, LocalDate birthdate) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthdate = birthdate;
		this.status = false; // 기본값: 정상 상태
	}
}

