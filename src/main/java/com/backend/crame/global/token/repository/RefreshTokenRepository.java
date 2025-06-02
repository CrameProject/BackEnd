package com.backend.crame.global.token.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.backend.crame.global.token.entity.RefreshToken;

public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken,String> {
}
