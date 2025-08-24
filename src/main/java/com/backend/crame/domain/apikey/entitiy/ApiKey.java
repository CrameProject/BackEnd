package com.backend.crame.domain.apikey.entitiy;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.backend.crame.global.utils.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "apikey")
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class ApiKey extends BaseTimeEntity {
	@Id
	private String key_uuid;

	private String user_uuid;

	@Setter
	private String nickname;


	private String publicKey;

	private String secretKey;

	@Builder
	private ApiKey(String key_uuid, String user_uuid, String nickname, String publicKey, String secretKey){
		this.key_uuid = key_uuid;
		this.user_uuid = user_uuid;
		this.nickname = nickname;
		this.publicKey = publicKey;
		this.secretKey = secretKey;
	}


}
