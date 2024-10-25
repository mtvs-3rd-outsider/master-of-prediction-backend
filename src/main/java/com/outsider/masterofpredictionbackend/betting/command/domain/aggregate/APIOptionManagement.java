package com.outsider.masterofpredictionbackend.betting.command.domain.aggregate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

@Service
@Slf4j
public class APIOptionManagement {

    @Value("${api.path}")
    private String apiKey;

    private final Map<APIBettingProductCategory, String> apiOptions = new EnumMap<>(APIBettingProductCategory.class);

    public APIOptionManagement(
            @Value("${api.option.korea-football}") String kFootball,
            @Value("${api.option.global-football}") String gFootball,
            @Value("${api.option.basketball}") String basketball,
            @Value("${api.option.korea-baseball}") String kBaseball,
            @Value("${api.option.global-baseball}") String gBaseball
    ) {
        apiOptions.put(APIBettingProductCategory.KOREA_FOOTBALL, kFootball);
        apiOptions.put(APIBettingProductCategory.GLOBAL_FOOTBALL, gFootball);
        apiOptions.put(APIBettingProductCategory.BASKETBALL, basketball);
        apiOptions.put(APIBettingProductCategory.KOREA_BASEBALL, kBaseball);
        apiOptions.put(APIBettingProductCategory.GLOBAL_BASEBALL, gBaseball);
    }

    public URI getApi(APIBettingProductCategory category, LocalDate localDate) {
        try {
            String date = "&fromDate=" + localDate + "&toDate=" + localDate;
            String apiOption = apiOptions.get(category);

            if (apiOption != null) {
                return new URI(apiKey + apiOption.replace("\"", "") + date);
            } else {
                log.error("Invalid category: {}", category);
                throw new IllegalArgumentException("Invalid category: " + category);
            }
        } catch (URISyntaxException e) {
            log.error("Invalid URI: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
