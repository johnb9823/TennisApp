package com.example.tennisapp.domain.report.controller;

import com.example.tennisapp.domain.member.entity.Member;
import com.example.tennisapp.domain.report.dto.request.ReportCreate;
import com.example.tennisapp.domain.report.service.ReportService;
import com.example.tennisapp.global.error.UnauthorizedException;
import com.example.tennisapp.global.success.ApiResponse;
import com.example.tennisapp.global.success.SuccessCode;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> reportContent(
            @RequestBody ReportCreate request,
            HttpSession session
    ) {
        Member reporter = getSessionMember(session);
        reportService.createReport(request, reporter);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.REPORT_SUCCESS));
    }

    private Member getSessionMember(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("LOGIN_MEMBER");
        if (loginMember == null) throw new UnauthorizedException("로그인이 필요합니다.");
        return loginMember;
    }
}
