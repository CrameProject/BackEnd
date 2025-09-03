package com.backend.crame.domain.quant.service;

import org.springframework.stereotype.Service;

import com.backend.crame.domain.quant.dto.ModelSelectionRequest;
import com.backend.crame.domain.quant.dto.SubscriptionUpdateRequest;
import com.backend.crame.domain.quant.dto.UserSubscriptionResponse;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuantUserService {

    private final UserRepository userRepository;

    // 사용자 구독 상태 업데이트
    public Mono<UserSubscriptionResponse> updateSubscription(CustomPrincipal principal, SubscriptionUpdateRequest request) {
        final String userId = principal.getUserId();

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
            .flatMap(user -> {
                user.setSubscribe(request.subscribe());
                return userRepository.save(user);
            })
            .map(user -> new UserSubscriptionResponse(
                user.getSubscribe(),
                user.getSelect_model()
            ));
    }

    // 사용자 선택 모델 업데이트
    public Mono<UserSubscriptionResponse> updateSelectedModel(CustomPrincipal principal, ModelSelectionRequest request) {
        final String userId = principal.getUserId();

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
            .flatMap(user -> {
                user.setSelect_model(request.selectedModel());
                return userRepository.save(user);
            })
            .map(user -> new UserSubscriptionResponse(
                user.getSubscribe(),
                user.getSelect_model()
            ));
    }

    // 사용자 구독 상태 조회
    public Mono<UserSubscriptionResponse> getSubscriptionStatus(CustomPrincipal principal) {
        final String userId = principal.getUserId();

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
            .map(user -> new UserSubscriptionResponse(
                user.getSubscribe(),
                user.getSelect_model()
            ));
    }
}
