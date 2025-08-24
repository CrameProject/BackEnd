package com.backend.crame.domain.user.dto;

import java.time.LocalDate;

public record UserInfoResponse(String name, LocalDate birthDate, String id, String email, Boolean isSocial) {
}
