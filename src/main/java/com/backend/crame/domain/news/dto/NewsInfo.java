package com.backend.crame.domain.news.dto;

import java.util.List;

public record NewsInfo(
        String newsUrl,
        String title,
        String imageUrl,
        String date,
        List<String> keywords,
        String publisher) {
}