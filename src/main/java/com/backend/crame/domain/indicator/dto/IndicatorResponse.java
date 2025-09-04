package com.backend.crame.domain.indicator.dto;

import java.util.List;

public record IndicatorResponse(
        List<IndicatorInfo> indicators) {
}