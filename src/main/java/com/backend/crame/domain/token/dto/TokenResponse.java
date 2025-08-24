package com.backend.crame.domain.token.dto;

public record TokenResponse(String accessToken, String refreshToken, String userName) {
}
