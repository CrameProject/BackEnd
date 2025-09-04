package com.backend.crame.domain.apikey.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 키 응답")
public record ApiKeyResponse(
    @Schema(description = "닉네임")
    String nickName, 
    
    @Schema(description = "공개 키")
    String publicKey, 
    
    @Schema(description = "비밀 키 (마스킹됨)")
    String secretKey,
    
    @Schema(description = "전략 정보", example = "AI 딥러닝")
    String strategyInfo
) {
}
