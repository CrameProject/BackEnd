package com.backend.crame.domain.google.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.google.service.GoogleOAuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth/google")
@Tag(name = "구글 로그인 API", description = "구글 로그인 관련 API입니다.")
@RequiredArgsConstructor
public class GoogleController {

	private final GoogleOAuthService googleOAuthService;

	@PostMapping("/login")
	public Mono<ResponseEntity<Map<String, Object>>> loginWithGoogle(@RequestParam("code") String code) {
		return googleOAuthService.loginWithGoogle(code)
			.map(response -> ResponseEntity.ok().body(response))
			.onErrorMap(e -> new IllegalArgumentException(e.getMessage()));
	}



}
