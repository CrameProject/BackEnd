package com.backend.crame.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	LOGIN_FAIL(HttpStatus.BAD_REQUEST,"로그인에 오류가 발생하였습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버에 오류가 발생하였습니다.");

	private final HttpStatus code;
	private final String message;


	ErrorCode(HttpStatus code, String message){
		this.code = code;
		this.message = message;
	}
}
