package com.backend.crame.domain.user.entitiy;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.backend.crame.domain.terms.Terms;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "user")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class User {
	@Id
	private String user_uuid;

	private String wallet_uuid;

	private String email;

	private UserStatus status;

	@Field("terms")
	private Terms terms;

	private Boolean subscribe;

	private String select_model;

	private String domain; // 어떤 소셜 로그인인지 분기

}
