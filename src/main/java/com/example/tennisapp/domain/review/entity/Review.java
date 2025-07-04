package com.example.tennisapp.domain.review.entity;

import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.reservation.entity.Reservation;
import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id", nullable = false, unique = true)
	private Reservation reservation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "court_id", nullable = false)
	private Court court;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private int rating;
}

