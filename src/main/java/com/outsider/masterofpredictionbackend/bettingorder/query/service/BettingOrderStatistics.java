package com.outsider.masterofpredictionbackend.bettingorder.query.service;

import com.outsider.masterofpredictionbackend.bettingorder.query.dto.*;
import com.outsider.masterofpredictionbackend.bettingorder.query.repository.BettingOptionRepository;
import com.outsider.masterofpredictionbackend.bettingorder.query.repository.BettingOrderQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BettingOrderStatistics {
    private final EntityManager entityManager;
    private final BettingOrderQueryRepository bettingOrderQueryRepository;
    private final BettingOptionRepository bettingOptionRepository;

    public BettingOrderStatistics(EntityManager entityManager, BettingOrderQueryRepository bettingOrderQueryRepository, BettingOptionRepository bettingOptionRepository) {
        this.entityManager = entityManager;
        this.bettingOrderQueryRepository = bettingOrderQueryRepository;
        this.bettingOptionRepository = bettingOptionRepository;
    }


    // 두 시간이 1분 이내인지 확인
    public static boolean isWithinOneMinute(LocalTime time1, LocalTime time2) {
        return time1.withSecond(0).withNano(0).equals(time2.withSecond(0).withNano(0));
    }

    public List<StatisticsDateTimeDTO> orderFilter(Long bettingId, LocalDate filterDate, LocalTime filterTime) {
        // Native Query 실행 - 반환 타입을 명시적으로 List<Object[]>로 지정
        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT CONCAT(bo.order_date, ' ', bo.order_time) AS time_slot, " +
                                "SUM(bo.point) AS total_points, bo.betting_option_id " +
                                "FROM betting_order bo " +
                                "WHERE bo.betting_id = ?1 " +
                                "AND (bo.order_date > ?2 OR (bo.order_date = ?2 AND bo.order_time >= ?3)) " +
                                "GROUP BY bo.betting_option_id, bo.order_date, bo.order_time " +
                                "ORDER BY bo.betting_option_id, bo.order_date, bo.order_time")
                .setParameter(1, bettingId)
                .setParameter(2, filterDate)
                .setParameter(3, filterTime)
                .getResultList();

        // 결과를 DTO 로 수동 매핑
        return results.stream()
                .map(result -> new StatisticsDateTimeDTO(
                        LocalDateTime.parse((String) result[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"))
                                .truncatedTo(ChronoUnit.SECONDS),  // 나노초 제거 (초 단위까지만)
                        (BigDecimal) result[1],  // total_points
                        (Long) result[2]         // betting_option_id
                ))
                .collect(Collectors.toList());
    }

    /**
     * 최근 1시간 동안의 베팅 주문 통계를 조회한다.
     * @param bettingId 베팅 ID
     * @return 1시간 베팅 주문 통계 목록
     */
    public Map<Long, Map<LocalDate, List<StatisticsTimeDTO>>> findBettingOrderHistoryInLastHour(Long bettingId) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        // 옵션 조회
        List<Long> optionIds = bettingOptionRepository.findBettingOptionIdByBettingId(bettingId);

        // 현재시간 - 1시간 전부터 현재 시간까지 1분단위로 시간객체 생성
        List<LocalDateTime> time_slot =  TimeSlotGenerator.generateMinuteIntervals(LocalDateTime.now(), 60, 1);

        // 1시간 전까지의 옵션별로 포인트 합계
        List<TotalPointsUntilAgo> prev = bettingOrderQueryRepository.totalPointsSumUntilOneHourAgo(bettingId, oneHourAgo.toLocalDate(), oneHourAgo.toLocalTime());

        Map<Long, Map<LocalDate, List<StatisticsTimeDTO>>>  retDTO = new HashMap<>();

        // 옵션과 시간객체로 retDTO 초기화 및 1시간 전까지의 주문 포인트 총합 적용
        for (Long optionId : optionIds){
            BigDecimal totalPoints = prev.stream()
                    .filter(dto -> dto.getBettingOptionId().equals(optionId))
                    .map(TotalPointsUntilAgo::getTotalPoints)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
            Map<LocalDate, List<StatisticsTimeDTO>> map = new HashMap<>();
            for (LocalDateTime dateTime : time_slot){
                LocalDate date = dateTime.toLocalDate();
                LocalTime time = dateTime.toLocalTime();
                if (map.containsKey(date)){
                    map.get(date).add(new StatisticsTimeDTO(time, totalPoints));
                } else {
                    List<StatisticsTimeDTO> list = new ArrayList<>();
                    list.add(new StatisticsTimeDTO(time, totalPoints));
                    map.put(date, list);
                }
            }
            retDTO.put(optionId, map);
        }

        // 최근 한 시간 주문데이터 불러오기
        List<StatisticsDateTimeDTO> oneHour = orderFilter(bettingId, oneHourAgo.toLocalDate(), oneHourAgo.toLocalTime());

        // 이전 데이터에 최근 한 시간 데이터 적용
        for (StatisticsDateTimeDTO dto : oneHour) {
            Long optionId = dto.getBettingOptionId();
            LocalDate date = dto.getTimeSlot().toLocalDate();
            LocalTime time = dto.getTimeSlot().toLocalTime();
            BigDecimal totalPoints = dto.getTotalPoints();

            if (!retDTO.containsKey(optionId)) {
                continue;
            }

            Map<LocalDate, List<StatisticsTimeDTO>> dateMap = retDTO.get(optionId);
            if (!dateMap.containsKey(date)) {
                continue;
            }

            List<StatisticsTimeDTO> list = dateMap.get(date);
            for (int i = 0; i < list.size(); i++) {
                StatisticsTimeDTO timeDTO = list.get(i);
                if (isWithinOneMinute(timeDTO.getTimeSlot(), time)) {
                    BigDecimal updatedPoints = timeDTO.getTotalPoints().add(totalPoints);


                    if (i > 0 && list.get(i - 1).getTotalPoints().compareTo(timeDTO.getTotalPoints()) == 0) {
                        updatedPoints = list.get(i - 1).getTotalPoints().add(totalPoints);
                    }

                    timeDTO.setTotalPoints(updatedPoints);
                    for (int j = i + 1; j < list.size(); j++) {
                        list.get(j).setTotalPoints(timeDTO.getTotalPoints());
                    }
                }
            }
        }

        aggregateTotalPointsAndCalculateRatio(retDTO);

        return  retDTO;
    }

    public static void aggregateTotalPointsAndCalculateRatio(Map<Long, Map<LocalDate, List<StatisticsTimeDTO>>> bettingData) {
        // First, aggregate totalPoints across all options for each timeSlot
        Map<LocalDate, Map<LocalTime, BigDecimal>> totalPointsByDateAndTimeSlot = new HashMap<>();

        for (Long optionId : bettingData.keySet()) {
            Map<LocalDate, List<StatisticsTimeDTO>> dateMap = bettingData.get(optionId);
            for (LocalDate date : dateMap.keySet()) {
                List<StatisticsTimeDTO> timeSlotList = dateMap.get(date);
                for (StatisticsTimeDTO timeSlotData : timeSlotList) {
                    LocalTime timeSlot = timeSlotData.getTimeSlot();
                    BigDecimal totalPoints = timeSlotData.getTotalPoints();

                    totalPointsByDateAndTimeSlot.computeIfAbsent(date, k -> new HashMap<>())
                            .merge(timeSlot, totalPoints, BigDecimal::add);
                }
            }
        }

        // Now, calculate the ratio for each option based on the aggregated total points
        for (Long optionId : bettingData.keySet()) {
            Map<LocalDate, List<StatisticsTimeDTO>> dateMap = bettingData.get(optionId);
            for (LocalDate date : dateMap.keySet()) {
                List<StatisticsTimeDTO> timeSlotList = dateMap.get(date);
                for (StatisticsTimeDTO timeSlotData : timeSlotList) {
                    LocalTime timeSlot = timeSlotData.getTimeSlot();
                    BigDecimal totalPoints = timeSlotData.getTotalPoints();
                    BigDecimal aggregatedTotalPoints = totalPointsByDateAndTimeSlot.get(date).get(timeSlot);

                    int ratio = aggregatedTotalPoints.compareTo(BigDecimal.ZERO) != 0 ? totalPoints.multiply(BigDecimal.valueOf(100)).divide(aggregatedTotalPoints, RoundingMode.HALF_UP).intValue() : 0;
                    timeSlotData.setRatio(ratio);
                }
            }
        }
    }

    /**
     * 5분 단위로 그룹핑된 베팅 주문 통계를 조회한다.
     * @param bettingId 베팅 ID
     * @return 5분단위 베팅 주문 통계 목록
     */
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
