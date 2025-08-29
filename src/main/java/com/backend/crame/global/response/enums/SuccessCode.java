package com.backend.crame.global.response.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다."),
	SIGNUP_SUCCESS(HttpStatus.OK, "회원가입에 성공하였습니다."),
	INDICATOR_SUCCESS(HttpStatus.OK, "경제 지표 조회에 성공하였습니다."),
	NEWS_SUCCESS(HttpStatus.OK, "뉴스 조회에 성공하였습니다.");

	private final HttpStatus code;
	private final String message;

	SuccessCode(HttpStatus code, String message) {
		this.code = code;
		this.message = message;
	}
}
