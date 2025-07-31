package com.example.tennisapp.domain.report.repository;

import com.example.tennisapp.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByReporter_MemberIdAndBoard_BoardId(Long reporterId, Long boardId);

    boolean existsByReporter_MemberIdAndComment_CommentId(Long reporterId, Long commentId);
}
