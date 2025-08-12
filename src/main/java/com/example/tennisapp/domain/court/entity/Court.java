package com.example.tennisapp.domain.court.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.tennisapp.domain.court.enums.CourtType;
import com.example.tennisapp.domain.courtImage.entity.CourtImage;
import com.example.tennisapp.domain.owner.entity.Owner;
import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Court extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courtId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private Owner owner;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CourtType courtType;

	@Column(columnDefinition = "TEXT")
	private String description;

	@OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourtImage> images = new ArrayList<>();

	public Court(Owner owner, String name, String address, CourtType courtType, String description) {
		this.owner = owner;
		this.name = name;
		this.address = address;
		this.courtType = courtType;
		this.description = description;
	}

	public void updateCourt(String name, String address, CourtType courtType, String description) {
		this.name = name;
		this.address = address;
		this.courtType = courtType;
		this.description = description;
	}

	/** Court <-> CourtImage 양방향 연관관계 편의 메서드 */
	public void addImage(CourtImage image) {
		this.images.add(image);
		image.setCourtInternal(this); // setter 대신 내부 메서드로만 court 세팅
	}

}

