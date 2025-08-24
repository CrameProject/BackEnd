package com.backend.crame.domain.google.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.backend.crame.domain.user.entitiy.Domain;
import com.backend.crame.domain.user.entitiy.User;
import com.backend.crame.domain.user.entitiy.UserRole;
import com.backend.crame.domain.user.entitiy.UserStatus;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;
import com.backend.crame.domain.token.repository.RefreshTokenRepository;
import com.backend.crame.domain.token.service.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleOAuthService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirectUri;


	private final WebClient webClient = WebClient.create();

	public Mono<Map<String, Object>> loginWithGoogle(String code) {
		return exchangeCodeForAccessToken(code)
			.flatMap(this::fetchUserInfo)
			.flatMap(this::handleUserInfo);
	}

	private Mono<String> exchangeCodeForAccessToken(String code) {
		String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);

		return webClient.post()
			.uri("https://oauth2.googleapis.com/token")
			.bodyValue(Map.of(
				"code", decodedCode,
				"client_id", clientId,
				"client_secret", clientSecret,
				"redirect_uri", redirectUri,
				"grant_type", "authorization_code"
			))
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
			.map(body -> {
				if (!body.containsKey("access_token")) {
					throw new BaseException(ErrorCode.LOGIN_FAIL);
				}
				return (String) body.get("access_token");
			});

	}

	private Mono<Map<String, Object>> fetchUserInfo(String accessToken) {
		return webClient.get()
			.uri("https://www.googleapis.com/oauth2/v3/userinfo")
			.headers(headers -> headers.setBearerAuth(accessToken))
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<>() {});
	}

	private Mono<Map<String, Object>> handleUserInfo(Map<String, Object> userInfo) {
		String email = (String) userInfo.get("email");
		if (email == null) {
			return Mono.error(new BaseException(ErrorCode.LOGIN_FAIL));
		}
		String uuid = UUID.randomUUID().toString();

		return userRepository.findByEmail(email)
			.flatMap(user -> {
				if (user.getStatus() == UserStatus.SUCCESS) {
					return jwtTokenProvider.createToken(user.getName(),user.getUser_uuid(),user.getUserRole().toString(),user.getDomain().toString())
						.map(tokenResponse ->{
							Map<String, Object> result = new HashMap<>();
							result.put("accessToken", tokenResponse.accessToken());
							result.put("refreshToken",tokenResponse.refreshToken());
							result.put("userName",tokenResponse.userName());
							result.put("isSignedUp", true);
							return result;
				});
				} else if(user.getStatus() == UserStatus.DELETED){
					return Mono.error(new BaseException(ErrorCode.ALREADY_SIGNOUT_USER));
				}else {
					Map<String, Object> result = new HashMap<>();
					result.put("message", "회원가입이 필요합니다");
					result.put("email", email);
					result.put("isSignedUp", false);
					return Mono.just(result);
				}
			})
			.switchIfEmpty(
				userRepository.save(User.builder()
						.user_uuid(uuid)
						.email(email)
						.status(UserStatus.PENDING)
						.domain(Domain.valueOf("GOOGLE"))
						.subscribe(false)
						.select_model("")
						.wallet_uuid("")
						.userRole(UserRole.ROLE_USER)
						.build())
					.thenReturn(Map.of(
						"message", "회원가입이 필요합니다",
						"email", email,
						"isSignedUp", false
					))
			);
	}


}
