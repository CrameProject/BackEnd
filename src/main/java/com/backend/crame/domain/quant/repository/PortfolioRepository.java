package com.backend.crame.domain.quant.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.backend.crame.domain.quant.entity.Portfolio;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioRepository extends ReactiveMongoRepository<Portfolio, String> {
    
    Flux<Portfolio> findAllByUserId(String userId);
    
    Mono<Portfolio> findByApiKeyId(String apiKeyId);
    
    Flux<Portfolio> findAllByUserIdOrderByCreatedAtDesc(String userId);
}
