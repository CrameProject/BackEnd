package com.backend.crame.domain.quant.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.quant.dto.TradingHistoryResponse;
import com.backend.crame.domain.quant.service.TradingHistoryService;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.dto.ResponseDto;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quant/trading-history")
@Tag(name = "거래 내역 관련 API", description = "사용자 거래 내역 조회 관련 API입니다.")
@RequiredArgsConstructor
public class TradingHistoryController {

    private final TradingHistoryService tradingHistoryService;

    @GetMapping()
    @Operation(summary = "거래 내역 조회 API", description = "사용자의 모든 거래 내역을 조회합니다.")
    public Mono<ResponseEntity<ResponseDto<List<TradingHistoryResponse>>>> getUserTradingHistory(
            @AuthenticationPrincipal CustomPrincipal principal) {
        return tradingHistoryService.getUserTradingHistory(principal)
            .map(data -> BaseResponse.success(SuccessCode.GET_SUCCESS, data));
    }

    @GetMapping("/period")
    @Operation(summary = "기간별 거래 내역 조회 API", description = "특정 기간의 거래 내역을 조회합니다.")
    public Mono<ResponseEntity<ResponseDto<List<TradingHistoryResponse>>>> getUserTradingHistoryByPeriod(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Parameter(description = "시작 날짜", example = "2025-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜", example = "2025-01-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return tradingHistoryService.getUserTradingHistoryByPeriod(principal, startDate, endDate)
            .map(data -> BaseResponse.success(SuccessCode.GET_SUCCESS, data));
    }
}
