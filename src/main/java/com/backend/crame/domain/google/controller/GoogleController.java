package com.backend.crame.domain.google.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.google.dto.SignUpRequest;
import com.backend.crame.domain.google.service.GoogleOAuthService;
import com.backend.crame.global.exception.BaseException;
import com.backend.crame.global.exception.ErrorCode;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/login/google")
@Tag(name = "구글 로그인 API", description = "구글 로그인 관련 API입니다.")
@RequiredArgsConstructor
public class GoogleController {

	private final GoogleOAuthService googleOAuthService;

	//로그인 할 때 사용하는 것이다.
	@PostMapping("")
	public Mono<ResponseEntity<Map<String, Object>>> loginWithGoogle(@RequestParam("code") String code) {
		return googleOAuthService.loginWithGoogle(code)
			.map(response -> ResponseEntity.ok().body(response))
			.onErrorMap(e -> new IllegalArgumentException(e.getMessage()));
	}

	//회원가입 완료될 때 추가정보들을 포함해서 제공해주는 형태가 되는 것이다.
	@PutMapping("/signup")
	public Mono<ResponseEntity<?>> completeSignup(@RequestBody SignUpRequest request) {
		return googleOAuthService.completeSignup(request)
			// 성공 시 BaseResponse 래핑하고 HTTP 200 OK
			.map(data -> BaseResponse.success(SuccessCode.SIGNUP_SUCCESS, data));
			// 비즈니스 에러는 BaseResponse.error 로 감싸서 4xx/5xx 응답;
	}


	@GetMapping("/logout")
	public Mono<ResponseEntity<?>> logout(@RequestParam("userId") String userId){
		return googleOAuthService.logOut(userId)
			.map(data->BaseResponse.success(SuccessCode.LOGOUT_SUCCESS,data));
	}


}
