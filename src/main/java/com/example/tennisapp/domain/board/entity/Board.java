package com.example.tennisapp.domain.board.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.tennisapp.domain.board.enums.PostCategory;
import com.example.tennisapp.domain.comment.entity.Comment;
import com.example.tennisapp.domain.member.entity.Member;
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

@Entity
@Getter
public class Board extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private int viewCount = 0;

	@Column(nullable = false)
	private int commentCount = 0;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PostCategory category;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();


	public Board() {}

	// 생성자 (생성, 수정 시 사용)
	public Board(Member member, String title, String content, PostCategory category) {
		this.member = member;
		this.title = title;
		this.content = content;
		this.category = category;
	}

	// 수정 메서드
	public void update(String title, String content, PostCategory category) {
		this.title = title;
		this.content = content;
		this.category = category;
	}

	public void increaseViewCount() {
		this.viewCount++;
	}

}

