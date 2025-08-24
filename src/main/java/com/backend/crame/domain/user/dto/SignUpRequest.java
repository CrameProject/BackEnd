package com.backend.crame.domain.user.dto;

import com.backend.crame.domain.user.entitiy.terms.Terms;

public record SignUpRequest(String email, String birthNumber,String phoneNum, String id, String password, String name, String walletUuid, Terms terms) {
}
