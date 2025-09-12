package com.backend.crame.domain.indicator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경제지표 조회 요청")
public record IndicatorRequest(
    @Schema(description = "년월 (YYYYMM 형식)", example = "202506")
    @NotBlank(message = "년월은 필수입니다")
    @Pattern(regexp = "\\d{6}", message = "년월은 YYYYMM 형식이어야 합니다")
    String yearMonth
) {
}