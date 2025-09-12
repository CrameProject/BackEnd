package com.backend.crame.domain.news.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "뉴스 정보")
public record NewsInfo(
        @Schema(description = "뉴스 URL", example = "https://news.example.com/bitcoin-prediction")
        String newsUrl,
        
        @Schema(description = "제목", example = "비트코인 가격 1억원 돌파 기대감 확산")
        String title,
        
        @Schema(description = "이미지 URL", example = "https://news.example.com/image/bitcoin.jpg")
        String imageUrl,
        
        @Schema(description = "날짜", example = "2025-06-15T09:30:00+09:00")
        String date,
        
        @Schema(description = "키워드 리스트", example = "[\"비트코인\", \"암호화폐\", \"투자\"]")
        List<String> keywords,
        
        @Schema(description = "발행사", example = "조선일보")
        String publisher) {
}