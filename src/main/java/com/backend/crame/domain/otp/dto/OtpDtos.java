package com.backend.crame.domain.otp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class OtpDtos {
	public record Request(@NotBlank @Email String email) {}
	public record Confirm(@NotBlank @Email String email, @NotBlank String code) {}
	public record BooleanResp(boolean success, String message) {}
	public record VoidResp(String message) {}
}