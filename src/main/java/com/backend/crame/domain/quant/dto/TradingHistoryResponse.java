package com.backend.crame.domain.quant.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "거래 내역 응답")
public record TradingHistoryResponse(
    @Schema(description = "거래 ID")
    String uuid,
    
    @Schema(description = "거래 날짜")
    LocalDate tradingDate,
    
    @Schema(description = "거래 시간")
    LocalTime tradingTime,
    
    @Schema(description = "트레이딩 종류", example = "알고리즘")
    String tradingType,
    
    @Schema(description = "전략 종류", example = "알고리즘 통계적 차익 거래")
    String strategyType,
    
    @Schema(description = "구분", example = "매수")
    String orderType,
    
    @Schema(description = "체결 여부", example = "체결")
    String executionStatus,
    
    @Schema(description = "유형", example = "시장가")
    String marketOrderType,
    
    @Schema(description = "주문량")
    BigDecimal orderQuantity,
    
    @Schema(description = "체결가")
    BigDecimal executionPrice,
    
    @Schema(description = "손익")
    BigDecimal profitLoss
) {
}
