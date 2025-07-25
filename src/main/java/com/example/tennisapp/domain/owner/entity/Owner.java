package com.example.tennisapp.domain.owner.entity;

import java.time.LocalDate;

import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ownerId;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Past(message = "생년월일은 과거 날짜여야 합니다.")
	@Column(nullable = false)
	private LocalDate birthdate;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private boolean isDeleted = false;


	public Owner(String name, String email, String password, LocalDate birthdate, String content) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthdate = birthdate;
		this.content = content;
	}

	public void softDelete() {
		this.isDeleted = true;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void updateProfile(String name, String content) {
		this.name = name;
		this.content = content;
	}
}

