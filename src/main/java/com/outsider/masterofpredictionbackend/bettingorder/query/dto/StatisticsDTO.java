package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {

    private Long bettingOptionId;
    // 날짜가 변경될 경우를 대비하여 Map 으로 변경
    private Map<Long, Map<LocalDate, List<StatisticsTimeDTO>>> statisticsTimeDTOMap;
}