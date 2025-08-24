package com.backend.crame.domain.user.dto;

import java.time.LocalDate;

public record ChangeUserRequest(String name, LocalDate birthDate) {
}
