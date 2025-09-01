package com.backend.crame.domain.quant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.quant.dto.PortfolioResponse;
import com.backend.crame.domain.quant.service.PortfolioService;
import com.backend.crame.domain.token.entity.CustomPrincipal;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.dto.ResponseDto;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quant/portfolio")
@Tag(name = "포트폴리오 관련 API", description = "사용자 포트폴리오 현황 관련 API입니다.")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping()
    @Operation(summary = "포트폴리오 현황 조회 API", description = "사용자의 포트폴리오 현황을 조회합니다.")
    public Mono<ResponseEntity<ResponseDto<List<PortfolioResponse>>>> getUserPortfolios(
            @AuthenticationPrincipal CustomPrincipal principal) {
        return portfolioService.getUserPortfolios(principal)
            .map(data -> BaseResponse.success(SuccessCode.GET_SUCCESS, data));
    }
}
