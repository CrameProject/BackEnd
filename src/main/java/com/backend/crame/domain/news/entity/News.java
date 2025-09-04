package com.backend.crame.domain.news.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "news")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News {

    @Id
    private String id;

    private String news_uuid;

    @Indexed
    private String title;

    private String summary;

    private String publisher;

    private String url;

    private String date;

    private String img_url;

    @Indexed
    private List<String> keywords;
}