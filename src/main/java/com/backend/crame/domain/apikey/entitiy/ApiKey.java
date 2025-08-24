package com.backend.crame.domain.apikey.entitiy;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
	private String uuid;

	private String userId;

	@Setter
	private String nickname;

	@Field("public_key")
	private String publicKey;

	@Setter
	private String keyVersion;

	private String secretKey;

	@Builder
	private ApiKey(String key_uuid, String user_uuid, String nickname, String publicKey, String secretKey,String keyVersion){
		this.uuid = key_uuid;
		this.userId = user_uuid;
		this.nickname = nickname;
		this.publicKey = publicKey;
		this.secretKey = secretKey;
		this.keyVersion = keyVersion;
	}


}
