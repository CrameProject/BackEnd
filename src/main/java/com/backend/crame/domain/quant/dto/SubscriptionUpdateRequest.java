package com.backend.crame.domain.quant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "구독 상태 업데이트 요청")
public record SubscriptionUpdateRequest(
    @Schema(description = "구독 여부", example = "true")
    Boolean subscribe
) {
}
