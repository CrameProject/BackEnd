package com.backend.crame.domain.quant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 구독 상태 응답")
public record UserSubscriptionResponse(
    @Schema(description = "구독 여부")
    Boolean subscribe,
    
    @Schema(description = "선택한 모델")
    String selectedModel
) {
}
