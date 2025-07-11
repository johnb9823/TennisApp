package com.example.tennisapp.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getHttpStatus();
	String getMessage();
}
