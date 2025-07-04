package com.example.tennisapp.domain.comment.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.tennisapp.domain.board.entity.Board;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private Comment parentComment; //부모 댓글 (대댓글일 경우 사용)

	@OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> childComments = new ArrayList<>();
	//대댓글 리스트 (cascade = ALL → 댓글 삭제 시 대댓글도 삭제)
	//댓글 삭제 시 대댓글도 같이 삭제 (orphanRemoval = true)
}

