package com.example.tennisapp.domain.report.entity;

import com.example.tennisapp.domain.board.entity.Board;
import com.example.tennisapp.domain.comment.entity.Comment;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.report.enums.ReportReason;
import com.example.tennisapp.global.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Report extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_id", nullable = false)
	private Member reporter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = true)
	private Board board;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id", nullable = true)
	private Comment comment;

	@Enumerated(EnumType.STRING)
	private ReportReason reason;

	private String description;

	public Report(Member reporter, Board board, Comment comment, ReportReason reason, String description) {
		this.reporter = reporter;
		this.board = board;
		this.comment = comment;
		this.reason = reason;
		this.description = description;
	}

	public Report() {}
}
