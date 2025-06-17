package com.backend.crame.global.token.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refreshToken")
public class RefreshToken {

	@Id
	private String id;
	private String userId;
	private String token;
	private Instant expiryDate;
}