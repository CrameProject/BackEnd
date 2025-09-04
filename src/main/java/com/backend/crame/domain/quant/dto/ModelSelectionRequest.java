package com.backend.crame.domain.quant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모델 선택 요청")
public record ModelSelectionRequest(
    @Schema(description = "선택한 모델", example = "AI 딥러닝")
    String selectedModel
) {
}
