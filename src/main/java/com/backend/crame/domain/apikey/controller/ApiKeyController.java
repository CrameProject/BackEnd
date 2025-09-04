package com.backend.crame.domain.apikey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.apikey.dto.ApiKeyDeleteResponse;
import com.backend.crame.domain.apikey.dto.ApiKeyRequest;
import com.backend.crame.domain.apikey.dto.ApiKeyResponse;
import com.backend.crame.domain.apikey.service.ApiKeyService;
import com.backend.crame.domain.token.dto.TokenResponse;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.domain.user.dto.SignUpRequest;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.dto.ResponseDto;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/apikey")
@Tag(name = "APIKEY 관련 API")
@RequiredArgsConstructor
public class ApiKeyController {

	private final ApiKeyService apiKeyService;

	//API KEY 추가
	@PostMapping()
	@Operation(summary = "APIKEY를 추가 API")
	public Mono<ResponseEntity<ResponseDto<ApiKeyResponse>>> makeKey(@AuthenticationPrincipal CustomPrincipal customPrincipal, @RequestBody
		ApiKeyRequest request) {
		return apiKeyService.postNewKey(customPrincipal,request)
			.map(data -> BaseResponse.success(SuccessCode.API_KEY_MAKE_SUCCESS, data));
	}

	//API KEY 삭제
	@DeleteMapping()
	@Operation(summary = "APIKEY를 삭제 API")
	public Mono<ResponseEntity<ResponseDto<ApiKeyDeleteResponse>>> deleteKey(@AuthenticationPrincipal CustomPrincipal customPrincipal,@RequestParam String publicKey) {
		return apiKeyService.deleteApiKey(customPrincipal,publicKey)
			.map(data -> BaseResponse.success(SuccessCode.API_KEY_DELETE_SUCCESS, data));
	}


	//API KEY 가져오기
	@GetMapping()
	@Operation(summary = "가지고 있는 APIKEY 가져오는 API -> pageable 필요 없을 것 같아서 그냥 list로 보냄")
	public Mono<ResponseEntity<ResponseDto<List<ApiKeyResponse>>>> getKeys(@AuthenticationPrincipal CustomPrincipal customPrincipal) {
		return apiKeyService.getKeysByUser(customPrincipal)
			.map(data -> BaseResponse.success(SuccessCode.API_KEY_GET_SUCCESS, data));
	}


}
