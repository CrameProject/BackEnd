package com.backend.crame.domain.indicator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경제지표 정보")
public record IndicatorInfo(
        @Schema(description = "날짜", example = "2025-06-15")
        String date,
        
        @Schema(description = "시간", example = "09:00")
        String time,
        
        @Schema(description = "국가", example = "KR")
        String country,
        
        @Schema(description = "중요도", example = "3")
        Integer importance,
        
        @Schema(description = "지표명", example = "한국은행 기준금리")
        String indicatorName,
        
        @Schema(description = "실제값", example = "3.50%")
        String actualValue,
        
        @Schema(description = "이전값", example = "3.25%")
        String previousValue,
        
        @Schema(description = "예측값", example = "3.50%")
        String forecastValue,
        
        @Schema(description = "변화량", example = "+0.25%p")
        String changeFromPrevious) {
}