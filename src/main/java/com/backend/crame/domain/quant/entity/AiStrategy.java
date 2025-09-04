package com.backend.crame.domain.quant.entity;

public enum AiStrategy {
    DEEP_LEARNING("딥러닝 트레이딩"),
    MACHINE_LEARNING("머신러닝 트레이딩");

    private final String displayName;

    AiStrategy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
