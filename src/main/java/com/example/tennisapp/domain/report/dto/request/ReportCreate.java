package com.example.tennisapp.domain.report.dto.request;

import com.example.tennisapp.domain.report.enums.ReportReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportCreate {

    private final Long boardId; // 게시글 ID

    private final Long commentId; // 댓글 ID

    @NotNull(message = "신고 사유는 필수입니다.")
    private final ReportReason reason; // 신고 사유 (enum)

    @Size(max = 500, message = "상세 설명은 500자 이내여야 합니다.")
    private final String description;  // 추가 설명
}
