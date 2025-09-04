package com.backend.crame.domain.otp.controller;

import com.backend.crame.domain.otp.dto.OtpDtos;
import com.backend.crame.domain.otp.service.OtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/auth/email", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "메일 인증 API")
public class OtpController {

	private final OtpService service;

	@PostMapping("/request")
	@Operation(description = "메일 인증하기 위해서 사용하는 API")
	public Mono<OtpDtos.VoidResp> request(@Valid @RequestBody OtpDtos.Request body) {
		String ip = "unknown";
		String ua = "crame-service";

		return service.requestCode(body, ip, ua)
			.thenReturn(new OtpDtos.VoidResp("인증 코드가 전송되었습니다."));
	}

	@PostMapping("/confirm")
	@Operation(description = "인증번호 주는 API")
	public Mono<OtpDtos.BooleanResp> confirm(@Valid @RequestBody OtpDtos.Confirm body) {
		String ip = "unknown";
		String ua = "crame-service";
		return service.confirmCode(body, ip, ua);
	}
}
