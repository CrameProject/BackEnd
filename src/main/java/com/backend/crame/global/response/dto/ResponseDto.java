package com.backend.crame.global.response.dto;

import org.springframework.http.HttpStatus;

public record ResponseDto<T>(
	HttpStatus code,
	String message,
	T result
) {
}
