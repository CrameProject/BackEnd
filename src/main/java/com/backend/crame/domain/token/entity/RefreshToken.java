package com.backend.crame.domain.token.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.backend.crame.global.utils.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refreshToken")
public class RefreshToken extends BaseTimeEntity {

	@Id
	private String id;
	private String userId;
	private String token;
	private Instant expiryDate;
}