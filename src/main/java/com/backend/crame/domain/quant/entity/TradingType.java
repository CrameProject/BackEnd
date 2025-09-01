package com.backend.crame.domain.quant.entity;

public enum TradingType {
    ALGORITHM("알고리즘"),
    AI("AI");

    private final String displayName;

    TradingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
