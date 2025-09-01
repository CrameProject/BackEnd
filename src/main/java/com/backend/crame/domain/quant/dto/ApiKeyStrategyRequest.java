package com.backend.crame.domain.quant.dto;

import com.backend.crame.domain.quant.entity.AiStrategy;
import com.backend.crame.domain.quant.entity.AlgorithmStrategy;
import com.backend.crame.domain.quant.entity.TradingType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 키 전략 설정 요청")
public record ApiKeyStrategyRequest(
    @Schema(description = "공개 키", example = "your-public-key")
    String publicKey,
    
    @Schema(description = "트레이딩 타입", example = "ALGORITHM")
    TradingType tradingType,
    
    @Schema(description = "알고리즘 전략 (트레이딩 타입이 ALGORITHM일 때)", example = "STATISTICAL_ARBITRAGE")
    AlgorithmStrategy algorithmStrategy,
    
    @Schema(description = "AI 전략 (트레이딩 타입이 AI일 때)", example = "DEEP_LEARNING")
    AiStrategy aiStrategy
) {
}
