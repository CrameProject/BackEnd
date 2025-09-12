package com.backend.crame.domain.indicator.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경제지표 응답")
public record IndicatorResponse(
        @Schema(description = "경제지표 리스트")
        List<IndicatorInfo> indicators) {
}