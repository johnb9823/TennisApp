package com.example.tennisapp.domain.courtImage.entity;

import com.example.tennisapp.domain.court.entity.Court;
import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CourtImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "court_id", nullable = false)
	private Court court;

	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	private boolean thumbnail;

	public CourtImage(Court court, String imageUrl, boolean thumbnail) {
		this.court = court;
		this.imageUrl = imageUrl;
		this.thumbnail = thumbnail;
	}

	public CourtImage(String imageUrl, boolean thumbnail) {
		this.imageUrl = imageUrl;
		this.thumbnail = thumbnail;
	}

	/** Court에서만 호출할 수 있도록 protected */
	public void setCourtInternal(Court court) {
		this.court = court;
	}

	public void setCourtFromCourtEntity(Court court) {
		this.court = court;
	}
}

