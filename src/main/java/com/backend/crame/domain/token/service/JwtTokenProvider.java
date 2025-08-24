package com.backend.crame.domain.token.service;

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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.backend.crame.domain.token.dto.TokenResponse;
import com.backend.crame.domain.token.entity.CustomOAuth2User;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.token.entity.RefreshToken;
import com.backend.crame.domain.token.repository.RefreshTokenRepository;
import com.backend.crame.domain.user.entitiy.Domain;
import com.backend.crame.domain.user.entitiy.UserRole;
import com.backend.crame.domain.user.entitiy.UserStatus;
import com.backend.crame.domain.user.repository.UserRepository;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;

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

	private final UserRepository userRepository;
	@Value("${spring.jwt.secret}")
	private String secret;

	@Value("${spring.jwt.access-token-duration}")
	private Duration accessTokenDuration;

	@Value("${spring.jwt.refresh-token-duration}")
	private Duration refreshTokenDuration;

	private final MacAlgorithm alg = Jwts.SIG.HS512;
	private SecretKey key;
	private final RefreshTokenRepository refreshTokenRepository;

	private static final String CLAIM_ROLE = "role";
	private static final String CLAIM_PROVIDER = "provider";


	@PostConstruct
	public void init() {
		try {
			this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new BaseException(ErrorCode.JWT_KEY_GENERATION_FAILED);
		}
	}
	public String createAccessToken(String userId, String role, String provider) {
		try {
			Date now = new Date();
			Date expiry = new Date(now.getTime() + accessTokenDuration.toMillis());

			return Jwts.builder()
				.subject(userId)
				.claim(CLAIM_ROLE, role)
				.claim(CLAIM_PROVIDER, provider)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(key, alg)
				.compact();
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public String createRefreshToken(String userId) {
		try {
			Date now = new Date();
			Date expiry = new Date(now.getTime() + refreshTokenDuration.toMillis());

			return Jwts.builder()
				.subject(userId)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(key, alg)
				.compact();
		} catch (Exception e) {
			throw new BaseException(ErrorCode.EXPIRED_REFRESH_TOKEN);
		}
	}




	public Mono<TokenResponse> createToken(String userName, String userId,String role, String domain) {
		try {
			String accessToken = createAccessToken(userId,role,domain);
			String refreshToken = createRefreshToken(userId);

			Instant expiryDate = Instant.now().plus(refreshTokenDuration);

			RefreshToken refreshTokenEntity = RefreshToken.builder()
				.userId(userId)
				.token(refreshToken)
				.expiryDate(expiryDate)
				.build();

			log.info("here is error");
			return refreshTokenRepository.save(refreshTokenEntity)
				.doOnError(err -> log.error("refreshToken 저장 중 에러", err))
				.thenReturn(new TokenResponse(accessToken,refreshToken,userName));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}


	public Mono<Authentication> getAuthentication(String token) {
		try {
			Claims claims = Jwts.parser()
				.verifyWith(key) // SecretKey
				.build()
				.parseSignedClaims(token)
				.getPayload();

			final String userId = claims.getSubject();
			final String roleFromToken = claims.get("role", String.class);
			final String providerFromToken = claims.get("provider", String.class);

			return userRepository.findById(userId)
				.switchIfEmpty(Mono.error(new BaseException(ErrorCode.USER_NOT)))
				.flatMap(user -> {
					if (user.getStatus() == UserStatus.DELETED) {
						return Mono.error(new BaseException(ErrorCode.ALREADY_SIGNOUT_USER));
					}

					UserRole dbRole = user.getUserRole();
					String normalizedRole = normalizeRole(
						dbRole != null ? dbRole.name() : roleFromToken
					);

					Domain provider = user.getDomain() != null
						? user.getDomain()
						: parseDomainOrDefault(providerFromToken, Domain.LOCAL);

					CustomPrincipal principal;
					if (provider == Domain.LOCAL) {
						principal = CustomPrincipal.fromLocal(
							user.getUser_uuid(),
							user.getName(),
							user.getPassword(),
							normalizedRole
						);
					} else {
						Map<String, Object> attrs = Map.of(
							"userId", user.getUser_uuid(),
							"email", user.getEmail()
						);
						principal = CustomPrincipal.fromOAuth(
							user.getUser_uuid(),
							normalizedRole,
							provider.name(),
							attrs,
							List.of(new SimpleGrantedAuthority(normalizedRole))
						);
					}

					return Mono.just(
						(Authentication) new UsernamePasswordAuthenticationToken(
							principal, token, principal.getAuthorities()
						)
					);
				})
				.onErrorMap(e -> (e instanceof BaseException) ? e : new BaseException(ErrorCode.LOGIN_FAIL));

		} catch (Exception e) {
			return Mono.error(new BaseException(ErrorCode.LOGIN_FAIL));
		}
	}

	private static String normalizeRole(String roleMaybeNull) {
		String role = (roleMaybeNull == null || roleMaybeNull.isBlank())
			? "ROLE_USER"
			: roleMaybeNull.trim();

		if (!role.startsWith("ROLE_")) {
			role = "ROLE_" + role;
		}
		return role;
	}

	private static Domain parseDomainOrDefault(String name, Domain def) {
		if (name == null || name.isBlank()) return def;
		try { return Domain.valueOf(name.trim().toUpperCase()); }
		catch (IllegalArgumentException ex) { return def; }
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

}
