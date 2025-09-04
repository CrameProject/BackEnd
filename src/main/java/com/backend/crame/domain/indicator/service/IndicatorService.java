package com.backend.crame.domain.indicator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.crame.domain.indicator.dto.IndicatorInfo;
import com.backend.crame.domain.indicator.dto.IndicatorRequest;
import com.backend.crame.domain.indicator.dto.IndicatorResponse;
import com.backend.crame.domain.indicator.entity.Indicator;
import com.backend.crame.domain.indicator.repository.IndicatorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndicatorService {

    private final IndicatorRepository indicatorRepository;

    public Mono<IndicatorResponse> getIndicatorsByMonth(IndicatorRequest request) {
        String yearMonth = request.yearMonth().substring(0, 4) + "-" + request.yearMonth().substring(4, 6);
        log.info("월별 지표 조회 - yearMonth: {}", yearMonth);

        return indicatorRepository.findByDateStartingWith(yearMonth)
            .collectList()
            .map(this::convertToResponse)
            .doOnSuccess(response -> 
                log.info("지표 조회 완료 - 개수: {}", response.indicators().size())
            );
    }

    private IndicatorResponse convertToResponse(List<Indicator> indicators) {
        List<IndicatorInfo> indicatorInfos = indicators.stream()
            .map(this::convertToIndicatorInfo)
            .toList();
        return new IndicatorResponse(indicatorInfos);
    }

    private IndicatorInfo convertToIndicatorInfo(Indicator indicator) {
        return new IndicatorInfo(
            indicator.getDate(),
            indicator.getTime(),
            indicator.getCountry(),
            indicator.getImportance(),
            indicator.getIndicator_name(),
            indicator.getActual_value(),
            indicator.getPrevious_value(),
            indicator.getForecast_value(),
            calculateChange(indicator.getActual_value(), indicator.getPrevious_value())
        );
    }

    private String calculateChange(String actualValue, String previousValue) {
        if (actualValue == null || previousValue == null) {
            return "N/A";
        }

        try {
            String actualNumStr = actualValue.replaceAll("[^\\d.-]", "");
            String previousNumStr = previousValue.replaceAll("[^\\d.-]", "");

            if (actualNumStr.isEmpty() || previousNumStr.isEmpty()) {
                return "N/A";
            }

            double actual = Double.parseDouble(actualNumStr);
            double previous = Double.parseDouble(previousNumStr);
            double change = actual - previous;

            return change >= 0 ? "+" + change : String.valueOf(change);
        } catch (NumberFormatException e) {
            return "N/A";
        }
    }

}