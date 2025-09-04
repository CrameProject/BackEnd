package com.backend.crame.domain.quant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.crame.domain.apikey.dto.ApiKeyResponse;
import com.backend.crame.domain.apikey.repository.ApiKeyRepository;
import com.backend.crame.domain.quant.dto.ApiKeyStrategyRequest;
import com.backend.crame.domain.quant.entity.TradingType;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;
import com.backend.crame.global.utils.AesGcmCrypto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuantApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final AesGcmCrypto aesGcmCrypto;

    /**
     * API 키에 전략 설정
     */
    public Mono<ApiKeyResponse> setApiKeyStrategy(CustomPrincipal principal, ApiKeyStrategyRequest request) {
        final String userId = principal.getUserId();

        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
            .then(apiKeyRepository.findAllByUserId(userId)
                .filter(apiKey -> apiKey.getPublicKey().equals(request.publicKey()))
                .next()
                .switchIfEmpty(Mono.error(new BaseException(ErrorCode.API_KEY_NOT_FOUND))))
            .flatMap(apiKey -> {
                // 전략 유효성 검증
                if (request.tradingType() == TradingType.ALGORITHM && request.algorithmStrategy() == null) {
                    return Mono.error(new BaseException(ErrorCode.INVALID_REQUEST));
                }
                if (request.tradingType() == TradingType.AI && request.aiStrategy() == null) {
                    return Mono.error(new BaseException(ErrorCode.INVALID_REQUEST));
                }

                // 전략 설정
                apiKey.setTradingType(request.tradingType());
                apiKey.setAlgorithmStrategy(request.algorithmStrategy());
                apiKey.setAiStrategy(request.aiStrategy());

                return apiKeyRepository.save(apiKey);
            })
            .map(savedApiKey -> {
                String decrypted = aesGcmCrypto.decrypt(savedApiKey.getSecretKey());
                String masked = maskSecret(decrypted);
                return new ApiKeyResponse(
                    savedApiKey.getNickname(),
                    savedApiKey.getPublicKey(),
                    masked,
                    savedApiKey.getStrategyDisplayName()
                );
            });
    }

    /**
     * 사용자의 API 키 목록과 전략 정보 조회
     */
    public Mono<List<ApiKeyResponse>> getApiKeysWithStrategy(CustomPrincipal principal) {
        final String userId = principal.getUserId();

        return apiKeyRepository.findAllByUserId(userId)
            .map(apiKey -> {
                String decrypted = aesGcmCrypto.decrypt(apiKey.getSecretKey());
                String masked = maskSecret(decrypted);
                return new ApiKeyResponse(
                    apiKey.getNickname(),
                    apiKey.getPublicKey(),
                    masked,
                    apiKey.getStrategyDisplayName()
                );
            })
            .collectList();
    }

    private String maskSecret(String secret) {
        if (secret == null || secret.isEmpty()) {
            return "";
        }
        int visible = Math.min(2, secret.length()); // 앞 2자리만 노출
        return secret.substring(0, visible) + "*".repeat(secret.length() - visible);
    }
}
