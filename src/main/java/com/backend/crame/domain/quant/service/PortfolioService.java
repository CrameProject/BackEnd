package com.backend.crame.domain.quant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.crame.domain.quant.dto.PortfolioResponse;
import com.backend.crame.domain.quant.repository.PortfolioRepository;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    // 사용자의 포트폴리오 현황 조회
    public Mono<List<PortfolioResponse>> getUserPortfolios(CustomPrincipal principal) {
        final String userId = principal.getUserId();

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
            .then(portfolioRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .map(portfolio -> new PortfolioResponse(
                    portfolio.getUuid(),
                    portfolio.getTradingType() != null ? portfolio.getTradingType().getDisplayName() : "",
                    portfolio.getStrategyDisplayName(),
                    portfolio.getAmount(),
                    portfolio.getProfitLoss(),
                    portfolio.getProfitRate()
                ))
                .collectList());
    }
}
