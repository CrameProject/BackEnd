package com.backend.crame.domain.apikey.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.backend.crame.domain.apikey.dto.ApiKeyDeleteResponse;
import com.backend.crame.domain.apikey.dto.ApiKeyRequest;
import com.backend.crame.domain.apikey.dto.ApiKeyResponse;
import com.backend.crame.domain.apikey.entitiy.ApiKey;
import com.backend.crame.domain.apikey.repository.ApiKeyRepository;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;
import com.backend.crame.global.utils.AesGcmCrypto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

	private final ApiKeyRepository apiKeyRepository;
	private final UserRepository userRepository;
	private final AesGcmCrypto aesGcmCrypto;

	public Mono<ApiKeyResponse> postNewKey(CustomPrincipal principal, ApiKeyRequest request) {
		final String uuid = UUID.randomUUID().toString();
		final String userId = principal.getUserId();
		final String publicKey = request.publicKey();

		return userRepository.findById(userId)
			.switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
			.then(apiKeyRepository.existsByUserIdAndPublicKey(userId, publicKey))
			.flatMap(exists -> {
				if (exists) {
					return Mono.error(new BaseException(ErrorCode.API_KEY_ALREADY_EXISTS));
				}
				String enc = aesGcmCrypto.encrypt(request.secretKey());
				ApiKey entity = ApiKey.builder()
					.key_uuid(uuid)
					.nickname(request.nickName())
					.publicKey(publicKey)
					.keyVersion(aesGcmCrypto.getKeyVersion())
					.secretKey(enc)
					.user_uuid(userId)
					.build();
				return apiKeyRepository.save(entity);
			})
			.onErrorMap(
				ex -> ex instanceof org.springframework.dao.DuplicateKeyException,
				ex -> new BaseException(ErrorCode.API_KEY_ALREADY_EXISTS)
			)
			.map(key -> {
				String decrypted = aesGcmCrypto.decrypt(key.getSecretKey());
				String masked = maskSecret(decrypted);
				return new ApiKeyResponse(key.getNickname(), key.getPublicKey(), masked, key.getStrategyDisplayName());
			});
	}



	public Mono<ApiKeyDeleteResponse> deleteApiKey(CustomPrincipal principal, String keyPublicKey) {
		final String userId = principal.getUserId();

		return userRepository.findById(userId)
			.switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
			.flatMap(u -> apiKeyRepository.deleteByUserIdAndPublicKey(userId, keyPublicKey))
			.flatMap(deleted -> {
				if (deleted > 0) {
					return Mono.just(new ApiKeyDeleteResponse("API KEY가 삭제되었습니다."));
				} else {
					return Mono.error(new BaseException(ErrorCode.API_KEY_NOT_FOUND));
				}
			});
	}


	public Mono<List<ApiKeyResponse>> getKeysByUser(CustomPrincipal customPrincipal) {
		return apiKeyRepository.findAllByUserId(customPrincipal.getUserId())
			.map(key -> {
				String decrypted = aesGcmCrypto.decrypt(key.getSecretKey());
				String masked = maskSecret(decrypted);
				return new ApiKeyResponse(
					key.getNickname(),
					key.getPublicKey(),
					masked,
					key.getStrategyDisplayName()
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
