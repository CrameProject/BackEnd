package com.backend.crame.domain.news.dto;

import java.util.List;

public record NewsResponse(
        List<NewsInfo> newsList) {
}