package com.backend.crame.domain.quant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.apikey.dto.ApiKeyResponse;
import com.backend.crame.domain.quant.dto.ApiKeyStrategyRequest;
import com.backend.crame.domain.quant.service.QuantApiKeyService;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.dto.ResponseDto;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quant/apikey")
@Tag(name = "Quant API 키 관련 API", description = "Quant API 키 전략 설정 관련 API입니다.")
@RequiredArgsConstructor
public class QuantApiKeyController {

    private final QuantApiKeyService quantApiKeyService;

    @PatchMapping("/strategy")
    @Operation(summary = "API 키 전략 설정 API", description = "특정 API 키에 트레이딩 전략을 설정합니다.")
    public Mono<ResponseEntity<ResponseDto<ApiKeyResponse>>> setApiKeyStrategy(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody ApiKeyStrategyRequest request) {
        return quantApiKeyService.setApiKeyStrategy(principal, request)
            .map(data -> BaseResponse.success(SuccessCode.UPDATE_SUCCESS, data));
    }

    @GetMapping("/strategy")
    @Operation(summary = "API 키 전략 정보 조회 API", description = "사용자의 모든 API 키와 설정된 전략 정보를 조회합니다.")
    public Mono<ResponseEntity<ResponseDto<List<ApiKeyResponse>>>> getApiKeysWithStrategy(
            @AuthenticationPrincipal CustomPrincipal principal) {
        return quantApiKeyService.getApiKeysWithStrategy(principal)
            .map(data -> BaseResponse.success(SuccessCode.GET_SUCCESS, data));
    }
}
