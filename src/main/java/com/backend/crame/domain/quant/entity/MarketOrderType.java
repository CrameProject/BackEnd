package com.backend.crame.domain.quant.entity;

public enum MarketOrderType {
    MARKET("시장가"),
    LIMIT("지정가"),
    STOP("스탑");

    private final String displayName;

    MarketOrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
