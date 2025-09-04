package com.backend.crame.domain.indicator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.indicator.dto.IndicatorRequest;
import com.backend.crame.domain.indicator.service.IndicatorService;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/indicators")
@Tag(name = "경제지표 API", description = "경제지표 관련 API입니다.")
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;

    @PostMapping
    @Operation(summary = "월별 경제지표 조회", description = "년월(YYYYMM)을 입력받아 해당 월의 모든 경제지표를 조회합니다.")
    // @SecurityRequirement(name = "bearerAuth")
    // @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<?>> getIndicators(@Valid @RequestBody IndicatorRequest request) {
        return indicatorService.getIndicatorsByMonth(request)
                .map(response -> BaseResponse.success(SuccessCode.INDICATOR_SUCCESS, response));
    }

}