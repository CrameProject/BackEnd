package com.backend.crame.domain.quant.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.backend.crame.global.utils.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "trading_history")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradingHistory extends BaseTimeEntity {

    @Id
    private String uuid;

    @Field("user_id")
    private String userId;

    @Field("api_key_id")
    private String apiKeyId;

    @Field("trading_date")
    private LocalDate tradingDate;

    @Field("trading_time")
    private LocalTime tradingTime;

    @Field("trading_type")
    private TradingType tradingType;

    @Field("algorithm_strategy")
    private AlgorithmStrategy algorithmStrategy;

    @Field("ai_strategy")
    private AiStrategy aiStrategy;

    @Field("order_type")
    private OrderType orderType;

    @Field("execution_status")
    private ExecutionStatus executionStatus;

    @Field("market_order_type")
    private MarketOrderType marketOrderType;

    @Field("order_quantity")
    private BigDecimal orderQuantity;

    @Setter
    @Field("execution_price")
    private BigDecimal executionPrice;

    @Setter
    @Field("profit_loss")
    private BigDecimal profitLoss;

    // 전략 정보를 문자열로 반환하는 헬퍼 메서드
    public String getStrategyDisplayName() {
        if (tradingType == TradingType.ALGORITHM && algorithmStrategy != null) {
            return tradingType.getDisplayName() + " " + algorithmStrategy.getDisplayName();
        } else if (tradingType == TradingType.AI && aiStrategy != null) {
            return tradingType.getDisplayName() + " " + aiStrategy.getDisplayName();
        }
        return tradingType != null ? tradingType.getDisplayName() : "";
    }
}
