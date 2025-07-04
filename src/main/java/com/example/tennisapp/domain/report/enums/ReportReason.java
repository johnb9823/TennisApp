package com.example.tennisapp.domain.report.enums;

public enum ReportReason {
	SPAM,             // 광고/도배
	INAPPROPRIATE,    // 부적절한 내용 (욕설, 혐오 등)
	HARASSMENT,       // 괴롭힘/인신 공격
	OFF_TOPIC,        // 주제와 맞지 않는 글
	SEXUAL_CONTENT,   // 성적인 내용
	VIOLENCE,         // 폭력적 내용
	ILLEGAL,          // 불법 정보 공유
	OTHER             // 기타 (필수적으로 상세 설명 필요)
}
