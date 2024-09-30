package com.outsider.masterofpredictionbackend.bettingorder.query.service;

import com.outsider.masterofpredictionbackend.bettingorder.query.dto.BettingOrderStatisticsDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BettingOrderStatistics {
    private final EntityManager entityManager;

    public BettingOrderStatistics(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<BettingOrderStatisticsDTO> findBettingOrderHistory(Long bettingId) {
        String sql = "SELECT " +
                "DATE(bo.order_date) AS dates, " +
                "TIME_FORMAT(SEC_TO_TIME((CEIL(TIME_TO_SEC(bo.order_time) / 300)) * 300), '%H:%i') AS time_slot, " +
                "bo.betting_option_id, " +
                "SUM(bo.point) AS total_points " +
                "FROM betting_order bo " +
                "WHERE bo.betting_id = :bettingId " +
                "GROUP BY dates, time_slot, bo.betting_option_id, bo.betting_id " +
                "ORDER BY bo.betting_option_id, dates, time_slot";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("bettingId", bettingId);
        List<Object[]> results = query.getResultList();

        // 수동으로 DTO로 변환
        List<BettingOrderStatisticsDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            LocalDate orderDate = ((java.sql.Date) row[0]).toLocalDate();  // java.sql.Date -> LocalDate로 변환
            String timeSlot = (String) row[1];

            // '24:00'을 '00:00'으로 처리하고, 날짜를 다음 날로 변경
            if ("24:00".equals(timeSlot)) {
                timeSlot = "00:00";
                orderDate = orderDate.plusDays(1);  // 날짜를 하루 증가
            }

            LocalTime orderTime = LocalTime.parse(timeSlot);  // time_slot은 String이므로 변환
            Long bettingOptionId = (Long) row[2];  // Long으로 캐스팅
            BigDecimal totalPoints = (BigDecimal) row[3];  // total_points는 BigDecimal로 캐스팅

            BettingOrderStatisticsDTO dto = new BettingOrderStatisticsDTO(orderDate, orderTime, bettingOptionId, totalPoints);
            dtos.add(dto);
        }
        log.info(dtos.toString());
        return dtos;
    }
}
