package com.backend.crame.domain.quant.entity;

import java.math.BigDecimal;

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

@Document(collection = "portfolio")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio extends BaseTimeEntity {

    @Id
    private String uuid;

    @Field("user_id")
    private String userId;

    @Field("api_key_id")
    private String apiKeyId;

    @Field("trading_type")
    private TradingType tradingType;

    @Field("algorithm_strategy")
    private AlgorithmStrategy algorithmStrategy;

    @Field("ai_strategy")
    private AiStrategy aiStrategy;

    @Setter
    @Field("amount")
    private BigDecimal amount;

    @Setter
    @Field("profit_loss")
    private BigDecimal profitLoss;

    @Setter
    @Field("profit_rate")
    private BigDecimal profitRate;

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
