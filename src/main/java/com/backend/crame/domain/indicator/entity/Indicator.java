package com.backend.crame.domain.indicator.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "economic_indicator")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Indicator {

    @Id
    private String id;

    private String economic_uuid;

    @Indexed
    private String date;

    private String time;

    private String country;

    private String currency;

    private String indicator_name;

    private String actual_value;

    private String forecast_value;

    private String previous_value;

    private Integer importance;

    private String importance_text;

    private String source;

    private LocalDateTime created_at;
}