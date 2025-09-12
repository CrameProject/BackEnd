package com.backend.crame.domain.news.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "뉴스 응답")
public record NewsResponse(
        @Schema(description = "뉴스 리스트")
        List<NewsInfo> newsList) {
}