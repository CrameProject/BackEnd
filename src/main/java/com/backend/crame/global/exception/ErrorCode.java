package com.backend.crame.global.exception;


import static org.springframework.http.HttpStatus.*;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {


	LOGIN_FAIL(HttpStatus.BAD_REQUEST,400,"로그인에 오류가 발생하였습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500,"서버에 오류가 발생하였습니다."),
	JWT_KEY_GENERATION_FAILED(HttpStatus.BAD_REQUEST,400,"JWT 키 생성에 실패하였습니다."),
	NO_REFRESH_TOKEN(UNAUTHORIZED,400, "리프레시 토큰이 없습니다."),
	LOGOUT_ERROR(BAD_REQUEST,400,"로그아웃에 실패하였습니다."),
	SIGNOUT_ERROR(BAD_REQUEST,404,"회원탈퇴에 실패하였습니다."),
	ALREADY_SIGNOUT_USER(BAD_REQUEST,400,"회원탈퇴된 회원입니다."),
	SIGNUP_ERROR(BAD_REQUEST,400,"회원가입에러입니다."),
	EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, 400,"만료된 토큰입니다."),

	USER_NOT(BAD_REQUEST,404,"유저를 찾을 수 없습니다."),
	ALREADY_REGISTERED_EMAIL(BAD_REQUEST,400,"이미 회원가입된 이메일입니다.");


	private final HttpStatus status;
	private final int code;
	private final String message;


	ErrorCode( HttpStatus status, int code, String message){
		this.code = code;
		this.status = status;
		this.message = message;
	}


}
