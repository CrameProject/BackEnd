package com.backend.crame.domain.user.entitiy;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.backend.crame.domain.user.entitiy.terms.Terms;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "user")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class User {

	@Id
	private String user_uuid;

	@Setter
	private String wallet_uuid;

	@Setter
	private String email;

	@Setter
	private String loginId;

	@Setter
	private String password;

	@Setter
	private String name; //이 이름을 설정하는 부분이 있으면 좋을 것 같긴하다

	@Setter
	private UserStatus status;

	@Setter
	private String birthNum;



	@Field("terms")
	@Setter
	private Terms terms; //약관에 대한 동의

	@Setter
	private Boolean subscribe; //구독제 결제 유무

	@Setter
	private String select_model;

	private Domain domain; // 어떤 소셜 로그인인지 분기

	private UserRole userRole;

}
