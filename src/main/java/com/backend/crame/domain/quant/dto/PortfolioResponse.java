package com.backend.crame.domain.quant.dto;

import java.math.BigDecimal;

import com.backend.crame.domain.quant.entity.TradingType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포트폴리오 응답")
public record PortfolioResponse(
    @Schema(description = "포트폴리오 ID")
    String uuid,
    
    @Schema(description = "트레이딩 종류", example = "알고리즘")
    String tradingType,
    
    @Schema(description = "전략 종류", example = "알고리즘 통계적 차익 거래")
    String strategyType,
    
    @Schema(description = "금액")
    BigDecimal amount,
    
    @Schema(description = "손익")
    BigDecimal profitLoss,
    
    @Schema(description = "수익률")
    BigDecimal profitRate
) {
}
