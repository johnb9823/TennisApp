package com.example.tennisapp.domain.report.service;

import com.example.tennisapp.domain.board.entity.Board;
import com.example.tennisapp.domain.board.repository.BoardRepository;
import com.example.tennisapp.domain.comment.entity.Comment;
import com.example.tennisapp.domain.comment.repository.CommentRepository;
import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.report.dto.request.ReportCreate;
import com.example.tennisapp.domain.report.entity.Report;
import com.example.tennisapp.domain.report.enums.ReportReason;
import com.example.tennisapp.domain.report.repository.ReportRepository;
import com.example.tennisapp.global.error.CustomRuntimeException;
import com.example.tennisapp.global.error.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createReport(ReportCreate request, Member reporter) {

        if (request.getBoardId() == null && request.getCommentId() == null) {
            throw new IllegalArgumentException("게시글 ID 또는 댓글 ID 중 적어도 하나는 필요합니다.");
        }

        if (request.getReason() == ReportReason.OTHER &&
                (request.getDescription() == null || request.getDescription().isBlank())) {
            throw new IllegalArgumentException("기타 신고 사유일 경우 상세 설명은 필수입니다.");
        }

        // 게시글 신고
        if (request.getBoardId() != null) {
            Board board = boardRepository.findById(request.getBoardId())
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

            if (board.getMember().getMemberId().equals(reporter.getMemberId())) {
                throw new CustomRuntimeException(ExceptionCode.CANNOT_REPORT_SELF);
            }

            if (reportRepository.existsByReporter_MemberIdAndBoard_BoardId(reporter.getMemberId(), request.getBoardId())) {
                throw new CustomRuntimeException(ExceptionCode.ALREADY_REPORTED);
            }

            Report boardReport = new Report(
                    reporter,
                    board,
                    null,
                    request.getReason(),
                    request.getDescription()
            );

            reportRepository.save(boardReport);
        }

        // 댓글 신고
        if (request.getCommentId() != null) {
            Comment comment = commentRepository.findById(request.getCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

            if (comment.getMember().getMemberId().equals(reporter.getMemberId())) {
                throw new CustomRuntimeException(ExceptionCode.CANNOT_REPORT_SELF);
            }

            if (reportRepository.existsByReporter_MemberIdAndComment_CommentId(reporter.getMemberId(), request.getCommentId())) {
                throw new CustomRuntimeException(ExceptionCode.ALREADY_REPORTED);
            }

            Report commentReport = new Report(
                    reporter,
                    null,
                    comment,
                    request.getReason(),
                    request.getDescription()
            );
            reportRepository.save(commentReport);
        }
    }

}
