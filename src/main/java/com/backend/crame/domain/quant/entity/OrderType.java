package com.backend.crame.domain.quant.entity;

public enum OrderType {
    BUY("매수"),
    SELL("매도");

    private final String displayName;

    OrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
