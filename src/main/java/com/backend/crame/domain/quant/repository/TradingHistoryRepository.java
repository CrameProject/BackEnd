package com.backend.crame.domain.quant.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.backend.crame.domain.quant.entity.TradingHistory;

import reactor.core.publisher.Flux;

public interface TradingHistoryRepository extends ReactiveMongoRepository<TradingHistory, String> {
    
    Flux<TradingHistory> findAllByUserId(String userId);
    
    Flux<TradingHistory> findAllByUserIdOrderByTradingDateDescTradingTimeDesc(String userId);
    
    Flux<TradingHistory> findAllByUserIdAndTradingDateBetweenOrderByTradingDateDescTradingTimeDesc(
        String userId, LocalDate startDate, LocalDate endDate);
    
    Flux<TradingHistory> findAllByApiKeyId(String apiKeyId);
}
