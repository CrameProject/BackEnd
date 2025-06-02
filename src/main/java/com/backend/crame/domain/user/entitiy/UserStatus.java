package com.backend.crame.domain.user.entitiy;

public enum UserStatus {
	//소셜 로그인 중에 대기상태를 두기 위해서 사용하는 부분
	PENDING("대기상태"),
	SUCCESS("회원가입 성공");

	UserStatus(String status){
		this.status = status;
	}

	String status;
}
