package com.backend.crame.global.response.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessCode {

	LOGIN_SUCCESS(HttpStatus.OK,"로그인에 성공하였습니다."),
	LOGOUT_SUCCESS(HttpStatus.OK,"로그아웃에 성공하였습니다."),
	SIGNOUT_SUCCESS(HttpStatus.OK,"회원탈퇴에 성공하였습니다."),
	INFOCHANGE_SUCCESS(HttpStatus.OK,"회원정보 수정에 성공하였습니다."),
	GETINFO_SUCCESS(HttpStatus.OK,"회원정보를 가져오는데 성공하였습니다."),
	SIGNUP_SUCCESS(HttpStatus.OK,"회원가입에 성공하였습니다."),
	NEWTOKEN_SUCCESS(HttpStatus.OK,"토큰 재발급에 성공하였습니다."),
	IDFOUND_SUCCESS(HttpStatus.OK,"아이디 찾기에 성공하였습니다."),
	PASSWORD_BEFORE_SUCCESS(HttpStatus.OK,"비밀번호 찾기(before)에 성공하였습니다."),

	API_KEY_MAKE_SUCCESS(HttpStatus.OK,"API 키 등록에 성공하였습니다."),
	API_KEY_DELETE_SUCCESS(HttpStatus.OK,"API 키 삭제에 성공하였습니다."),
	API_KEY_GET_SUCCESS(HttpStatus.OK,"API 키 조회에 성공하였습니다."),

	// Quant 관련 성공 코드
	UPDATE_SUCCESS(HttpStatus.OK,"업데이트에 성공하였습니다."),
	GET_SUCCESS(HttpStatus.OK,"조회에 성공하였습니다."),
  
  // 지표 관련 성공 코드
  INDICATOR_SUCCESS(HttpStatus.OK, "경제 지표 조회에 성공하였습니다."),
	NEWS_SUCCESS(HttpStatus.OK, "뉴스 조회에 성공하였습니다.");

	private final HttpStatus code;
	private final String message;

	SuccessCode(HttpStatus code, String message) {
		this.code = code;
		this.message = message;
	}
}
