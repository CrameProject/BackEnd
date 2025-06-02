package com.backend.crame.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	LOGIN_FAIL(HttpStatus.BAD_REQUEST,"로그인에 오류가 발생하였습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버에 오류가 발생하였습니다."),
	JWT_KEY_GENERATION_FAILED(HttpStatus.BAD_REQUEST,"JWT 키 생성에 실패하였습니다."),
	NO_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰이 없습니다."),
	EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "만료된 토큰입니다.");

	;

	private final HttpStatus code;
	private final String message;


	ErrorCode(HttpStatus code, String message){
		this.code = code;
		this.message = message;
	}
}
