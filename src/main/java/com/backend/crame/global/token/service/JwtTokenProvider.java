package com.backend.crame.global.token.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;
import com.backend.crame.global.token.dto.TokenResponse;
import com.backend.crame.global.token.entity.CustomOAuth2User;
import com.backend.crame.global.token.entity.RefreshToken;
import com.backend.crame.global.token.repository.RefreshTokenRepository;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${spring.jwt.secret}")
	private String secret;

	@Value("${spring.jwt.access-token-duration}")
	private Duration accessTokenDuration;

	@Value("${spring.jwt.refresh-token-duration}")
	private Duration refreshTokenDuration;

	private final MacAlgorithm alg = Jwts.SIG.HS512;
	private SecretKey key;
	private final RefreshTokenRepository refreshTokenRepository;

	@PostConstruct
	public void init() {
		try {
			this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new BaseException(ErrorCode.JWT_KEY_GENERATION_FAILED);
		}
	}

	public String createAccessToken(String userId) {
		try {
			Date now = new Date();
			Date expiry = new Date(now.getTime() + accessTokenDuration.toMillis());

			String token = Jwts.builder()
				.claims(Map.of("sub", userId))
				.issuedAt(now)
				.expiration(expiry)
				.signWith(key, alg)
				.compact();
			return token;
		} catch (Exception e) {
			throw new BaseException(ErrorCode.JWT_KEY_GENERATION_FAILED);
		}
	}

	public String createRefreshToken(String userId) {
		try {
			Date now = new Date();
			Date expiry = new Date(now.getTime() + refreshTokenDuration.toMillis());

			String token = Jwts.builder()
				.claims(Map.of("sub", userId))
				.issuedAt(now)
				.expiration(expiry)
				.signWith(key, alg)
				.compact();
			return token;
		} catch (Exception e) {
			throw new BaseException(ErrorCode.EXPIRED_REFRESH_TOKEN);
		}
	}

	public String getUserIdFromToken(String token) {
		try {
			return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
		} catch (JwtException e) {
			throw new BaseException(ErrorCode.LOGIN_FAIL);
		}
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//이제 이미 있는 회원에 대한 로직을 조금 변경해야 한다.
	//이미 로그인 되어있고 토큰이 있는 사람에 대한 로직을 조금 다르게 처리해야 함
	//번들로 만들지 않고 새로운 유저에겐 회원가입을 하게끔 정보를 제공해줌 -> 이 로직을 따로 받아야하는건가?
	public Mono<TokenResponse> createToken(String userId) {
		try {
			String accessToken = createAccessToken(userId);
			String refreshToken = createRefreshToken(userId);

			Instant expiryDate = Instant.now().plus(refreshTokenDuration);

			RefreshToken refreshTokenEntity = RefreshToken.builder()
				.userId(userId)
				.token(refreshToken)
				.expiryDate(expiryDate)
				.build();

			return refreshTokenRepository.save(refreshTokenEntity)
				.thenReturn(new TokenResponse(accessToken));
		} catch (Exception e) {
			return Mono.error(new BaseException(ErrorCode.LOGIN_FAIL));
		}
	}


	public Mono<Authentication> getAuthentication(String token) {
		try {
			Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();

			String userId = claims.getSubject();
			Map<String, Object> attributes = Map.of("userId", userId);
			CustomOAuth2User principal = new CustomOAuth2User(attributes, userId);
			Authentication auth = new UsernamePasswordAuthenticationToken(principal, token, List.of());
			return Mono.just(auth);
		} catch (Exception e) {
			return Mono.empty();
		}
	}


}
