package com.backend.crame.domain.news.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record NewsRequest(
    @NotBlank(message = "키워드는 필수입니다") 
    String keyword,
    
    @Min(value = 0, message = "페이지는 0 이상이어야 합니다") 
    int page) {
}