package com.backend.crame.domain.otp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.backend.crame.domain.otp.dto.EmailVerificationAudit;

public interface EmailVerificationAuditRepository extends ReactiveMongoRepository<EmailVerificationAudit,String> {
}
