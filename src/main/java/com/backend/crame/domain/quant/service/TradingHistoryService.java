package com.backend.crame.domain.quant.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.crame.domain.quant.dto.TradingHistoryResponse;
import com.backend.crame.domain.quant.repository.TradingHistoryRepository;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradingHistoryService {

    private final TradingHistoryRepository tradingHistoryRepository;
    private final UserRepository userRepository;

    // 사용자의 모든 거래 내역 조회
    public Mono<List<TradingHistoryResponse>> getUserTradingHistory(CustomPrincipal principal) {
        final String userId = principal.getUserId();

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
            .then(tradingHistoryRepository.findAllByUserIdOrderByTradingDateDescTradingTimeDesc(userId)
                .map(this::mapToResponse)
                .collectList());
    }

    // 특정 기간의 거래 내역 조회
    public Mono<List<TradingHistoryResponse>> getUserTradingHistoryByPeriod(
            CustomPrincipal principal, LocalDate startDate, LocalDate endDate) {
        final String userId = principal.getUserId();

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
            .then(tradingHistoryRepository.findAllByUserIdAndTradingDateBetweenOrderByTradingDateDescTradingTimeDesc(
                userId, startDate, endDate)
                .map(this::mapToResponse)
                .collectList());
    }

    private TradingHistoryResponse mapToResponse(com.backend.crame.domain.quant.entity.TradingHistory history) {
        return new TradingHistoryResponse(
            history.getUuid(),
            history.getTradingDate(),
            history.getTradingTime(),
            history.getTradingType() != null ? history.getTradingType().getDisplayName() : "",
            history.getStrategyDisplayName(),
            history.getOrderType() != null ? history.getOrderType().getDisplayName() : "",
            history.getExecutionStatus() != null ? history.getExecutionStatus().getDisplayName() : "",
            history.getMarketOrderType() != null ? history.getMarketOrderType().getDisplayName() : "",
            history.getOrderQuantity(),
            history.getExecutionPrice(),
            history.getProfitLoss()
        );
    }
}
