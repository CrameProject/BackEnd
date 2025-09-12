package com.backend.crame.domain.news.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "뉴스 조회 요청")
public record NewsRequest(
    @Schema(description = "검색 키워드", example = "비트코인")
    @NotBlank(message = "키워드는 필수입니다") 
    String keyword,
    
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    @Min(value = 0, message = "페이지는 0 이상이어야 합니다") 
    int page) {
}