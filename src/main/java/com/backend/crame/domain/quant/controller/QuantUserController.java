package com.backend.crame.domain.quant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.quant.dto.ModelSelectionRequest;
import com.backend.crame.domain.quant.dto.SubscriptionUpdateRequest;
import com.backend.crame.domain.quant.dto.UserSubscriptionResponse;
import com.backend.crame.domain.quant.service.QuantUserService;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.dto.ResponseDto;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quant/user")
@Tag(name = "Quant 사용자 관련 API", description = "Quant 사용자 구독 및 모델 선택 관련 API입니다.")
@RequiredArgsConstructor
public class QuantUserController {

    private final QuantUserService quantUserService;

    @PatchMapping("/subscription")
    @Operation(summary = "구독 상태 업데이트 API", description = "프리미엄 구독 상태를 업데이트합니다.")
    public Mono<ResponseEntity<ResponseDto<UserSubscriptionResponse>>> updateSubscription(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody SubscriptionUpdateRequest request) {
        return quantUserService.updateSubscription(principal, request)
            .map(data -> BaseResponse.success(SuccessCode.UPDATE_SUCCESS, data));
    }

    @PatchMapping("/model")
    @Operation(summary = "선택 모델 업데이트 API", description = "사용자가 선택한 트레이딩 모델을 업데이트합니다.")
    public Mono<ResponseEntity<ResponseDto<UserSubscriptionResponse>>> updateSelectedModel(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody ModelSelectionRequest request) {
        return quantUserService.updateSelectedModel(principal, request)
            .map(data -> BaseResponse.success(SuccessCode.UPDATE_SUCCESS, data));
    }

    @GetMapping("/subscription")
    @Operation(summary = "구독 상태 조회 API", description = "현재 사용자의 구독 상태와 선택 모델을 조회합니다.")
    public Mono<ResponseEntity<ResponseDto<UserSubscriptionResponse>>> getSubscriptionStatus(
            @AuthenticationPrincipal CustomPrincipal principal) {
        return quantUserService.getSubscriptionStatus(principal)
            .map(data -> BaseResponse.success(SuccessCode.GET_SUCCESS, data));
    }
}
