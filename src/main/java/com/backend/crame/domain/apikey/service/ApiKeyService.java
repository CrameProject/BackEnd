package com.backend.crame.domain.apikey.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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

	public Mono<Void> postNewKey(CustomPrincipal principal, ApiKeyRequest request) {
		final String uuid = UUID.randomUUID().toString();

		return userRepository.findById(principal.getUserId())
			.switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
			.flatMap(user -> {
				String enc = aesGcmCrypto.encrypt(request.secretKey());
				ApiKey entity = ApiKey.builder()
					.key_uuid(uuid)
					.nickname(request.nickName())
					.publicKey(request.publicKey())
					.keyVersion(aesGcmCrypto.getKeyVersion())
					.secretKey(enc)
					.user_uuid(principal.getUserId())
					.build();
				return apiKeyRepository.save(entity);
			})
			.then();
	}


	public Mono<Void> deleteApiKey(CustomPrincipal customPrincipal, String key_uuid){
		return userRepository.findById(customPrincipal.getUserId())
			.switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
			.flatMap(user ->
				apiKeyRepository.deleteByUuidAndUserId(key_uuid,user.getUser_uuid())
			).then();
	}

	public Mono<List<ApiKeyResponse>> getKeysByUser(CustomPrincipal customPrincipal) {
		return apiKeyRepository.findAllByUserId(customPrincipal.getUserId())
			.map(key -> {
				String decrypted = aesGcmCrypto.decrypt(key.getSecretKey());
				String masked = maskSecret(decrypted);
				return new ApiKeyResponse(
					key.getNickname(),
					key.getPublicKey(),
					masked
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
