package com.backend.crame.domain.user.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.crame.domain.user.entitiy.User;

import reactor.core.publisher.Mono;


public interface UserRepository extends ReactiveMongoRepository<User,String> {

	Mono<User> findByEmail(String email);
	Mono<User> findByLoginId(String id);

}
