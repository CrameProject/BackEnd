package com.backend.crame.domain.token.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.backend.crame.domain.token.entity.RefreshToken;

import reactor.core.publisher.Mono;


public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken,String> {

	Mono<RefreshToken> deleteByUserId(String userId);

}
