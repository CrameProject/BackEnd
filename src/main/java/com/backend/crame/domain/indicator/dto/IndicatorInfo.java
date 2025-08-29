package com.backend.crame.domain.indicator.dto;

public record IndicatorInfo(
        String date,
        String time,
        String country,
        Integer importance,
        String indicatorName,
        String actualValue,
        String previousValue,
        String forecastValue,
        String changeFromPrevious) {
}