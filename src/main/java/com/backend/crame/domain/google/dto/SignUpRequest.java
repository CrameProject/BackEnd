package com.backend.crame.domain.google.dto;

import com.backend.crame.domain.terms.Terms;

public record SignUpRequest(String email, String name, String walletUuid, Terms terms) {
}
