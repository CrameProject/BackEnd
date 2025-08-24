package com.backend.crame.domain.otp.dto;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("email_verification_audit")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationAudit {
	@Id
	private String id;
	private String email;
	private String purpose;
	private AuditEvent event;
	private String reason;
	private String ip;
	private String userAgent;
	private Instant createdAt;
}