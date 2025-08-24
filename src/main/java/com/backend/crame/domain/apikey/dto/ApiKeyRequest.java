package com.backend.crame.domain.apikey.dto;

public record ApiKeyRequest(String publicKey, String secretKey, String nickName) {
}
