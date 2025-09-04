package com.backend.crame.domain.indicator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record IndicatorRequest(
    @NotBlank(message = "년월은 필수입니다")
    @Pattern(regexp = "\\d{6}", message = "년월은 YYYYMM 형식이어야 합니다")
    String yearMonth
) {
}