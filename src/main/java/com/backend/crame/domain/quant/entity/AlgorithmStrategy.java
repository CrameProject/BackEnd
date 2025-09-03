package com.backend.crame.domain.quant.entity;

public enum AlgorithmStrategy {
    STATISTICAL_ARBITRAGE("통계적 차익 거래"),
    TREND_FOLLOWING("추세 추종 전략"),
    MEAN_REVERSION("평균회귀 전략"),
    HIGH_FREQUENCY("고빈도 거래 전략");

    private final String displayName;

    AlgorithmStrategy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
