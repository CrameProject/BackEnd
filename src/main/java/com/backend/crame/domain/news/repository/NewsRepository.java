package com.backend.crame.domain.news.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.backend.crame.domain.news.entity.News;

import reactor.core.publisher.Flux;

public interface NewsRepository extends ReactiveMongoRepository<News, String> {
    
    Flux<News> findByKeywordsContaining(String keyword);
}