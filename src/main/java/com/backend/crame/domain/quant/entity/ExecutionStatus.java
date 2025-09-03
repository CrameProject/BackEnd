package com.backend.crame.domain.quant.entity;

public enum ExecutionStatus {
    PENDING("대기"),
    EXECUTED("체결"),
    CANCELLED("취소");

    private final String displayName;

    ExecutionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
