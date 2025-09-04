package com.backend.crame.domain.user.dto;

public record NewPasswordRequest(
	String uuid,
	String newPassword
) {
}
