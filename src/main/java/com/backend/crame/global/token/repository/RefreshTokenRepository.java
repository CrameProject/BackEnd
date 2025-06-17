package com.backend.crame.global.token.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.crame.domain.user.entitiy.User;
import com.backend.crame.global.token.entity.RefreshToken;

import reactor.core.publisher.Mono;


public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken,String> {

	Mono<RefreshToken> deleteByUserId(String userId);
}
