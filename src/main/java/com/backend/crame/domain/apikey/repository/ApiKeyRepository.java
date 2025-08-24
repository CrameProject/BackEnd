package com.backend.crame.domain.apikey.repository;



import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.backend.crame.domain.apikey.entitiy.ApiKey;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApiKeyRepository extends ReactiveMongoRepository<ApiKey,String> {

	Mono<Long> deleteByUserIdAndPublicKey(String userId,String publicKey);
	Flux<ApiKey> findAllByUserId(String userId);
	Mono<Boolean> existsByUserIdAndPublicKey(String userUuid, String publicKey);
}
