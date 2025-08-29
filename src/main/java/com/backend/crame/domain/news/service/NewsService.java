package com.backend.crame.domain.news.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.crame.domain.news.dto.NewsInfo;
import com.backend.crame.domain.news.dto.NewsRequest;
import com.backend.crame.domain.news.dto.NewsResponse;
import com.backend.crame.domain.news.entity.News;
import com.backend.crame.domain.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public Mono<NewsResponse> getNewsByKeyword(NewsRequest request) {
        log.info("키워드별 뉴스 조회 - keyword: {}, page: {}", request.keyword(), request.page());

        int skip = request.page() * 10;
        
        return newsRepository.findByKeywordsContaining(request.keyword())
            .skip(skip)
            .take(10)
            .collectList()
            .map(this::convertToResponse)
            .doOnSuccess(response -> 
                log.info("뉴스 조회 완료 - page: {}, 개수: {}", request.page(), response.newsList().size())
            );
    }

    private NewsResponse convertToResponse(List<News> newsList) {
        List<NewsInfo> newsInfos = newsList.stream()
            .map(this::convertToNewsInfo)
            .toList();
        return new NewsResponse(newsInfos);
    }

    private NewsInfo convertToNewsInfo(News news) {
        return new NewsInfo(
            news.getUrl(),
            news.getTitle(),
            news.getImg_url(),
            news.getDate(),
            news.getKeywords(),
            news.getPublisher()
        );
    }
}