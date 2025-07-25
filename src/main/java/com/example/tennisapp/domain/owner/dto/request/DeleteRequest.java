package com.example.tennisapp.domain.owner.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Jackson이 필요로 함
@AllArgsConstructor
public class DeleteRequest {

	@NotBlank(message = "현재 비밀번호를 입력해주세요.")
	private String password;


}
