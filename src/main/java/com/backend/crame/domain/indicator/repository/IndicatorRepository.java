package com.backend.crame.domain.indicator.repository;

import com.backend.crame.domain.indicator.entity.Indicator;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IndicatorRepository extends ReactiveMongoRepository<Indicator, String> {
    
    Flux<Indicator> findByDateStartingWith(String yearMonth);
    
    @Query("{'date': {'$regex': '^?0'}}")
    Flux<Indicator> findByDateRegex(String yearMonth);
}