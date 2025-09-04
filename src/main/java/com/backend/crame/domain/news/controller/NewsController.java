package com.backend.crame.domain.news.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.crame.domain.news.dto.NewsRequest;
import com.backend.crame.domain.news.service.NewsService;
import com.backend.crame.global.response.BaseResponse;
import com.backend.crame.global.response.enums.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/news")
@Tag(name = "뉴스 API", description = "뉴스 관련 API입니다.")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    @Operation(summary = "키워드별 뉴스 조회", description = "키워드와 페이지를 입력받아 해당 키워드가 포함된 뉴스 10개를 조회합니다. (page: 0부터 시작)")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<?>> getNewsByKeyword(@Valid @RequestBody NewsRequest request) {
        return newsService.getNewsByKeyword(request)
                .map(response -> BaseResponse.success(SuccessCode.NEWS_SUCCESS, response));
    }
}