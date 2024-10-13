package com.outsider.masterofpredictionbackend.bettingorder.query.service;

import com.outsider.masterofpredictionbackend.betting.query.dto.BettingOptionDTO;
import com.outsider.masterofpredictionbackend.betting.query.repository.BettingOptionQueryRepository;
import com.outsider.masterofpredictionbackend.betting.query.repository.BettingQueryRepository;
import com.outsider.masterofpredictionbackend.bettingorder.query.dto.*;
import com.outsider.masterofpredictionbackend.bettingorder.query.repository.BettingOrderQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BettingOrderQueryService {

    private final BettingOrderQueryRepository bettingOrderQueryRepository;
    private final BettingQueryRepository bettingQueryRepository;
    private final BettingOrderStatistics bettingOrderStatistics;
    private final BettingOptionQueryRepository bettingOptionQueryRepository;

    public BettingOrderQueryService(BettingOrderQueryRepository bettingOrderQueryRepository, BettingQueryRepository bettingQueryRepository, BettingOrderStatistics bettingOrderStatistics, BettingOptionQueryRepository bettingOptionQueryRepository, BettingOptionQueryRepository bettingOptionQueryRepository1) {
        this.bettingOrderQueryRepository = bettingOrderQueryRepository;
        this.bettingQueryRepository = bettingQueryRepository;
        this.bettingOrderStatistics = bettingOrderStatistics;
        this.bettingOptionQueryRepository = bettingOptionQueryRepository1;
    }

    public Object findUserOrderHistory(Long userId, Long bettingId){
        return bettingOrderQueryRepository.findUserOrderHistory(userId, bettingId);
    }

    public List<ActivityDTO> findBettingActivity(Long bettingId) {
        return bettingOrderQueryRepository.findActivity(bettingId);
    }

    public Map<Long, List<TopHolderDTO>> findBettingTopHolders(Long bettingId) {
        List<TopHolderDTO> results = bettingOrderQueryRepository.findTopHolders(bettingId);
        return results.stream()
                .collect(Collectors.groupingBy(
                        TopHolderDTO::getBettingOptionId
                ));
    }

    public List<RatioDTO> findBettingProductOptionsRatio(Long bettingId) {
        return bettingOrderQueryRepository.findBettingProductOptionsRatio(bettingId);
    }

    public Map<Long, List<BettingOrderStatisticsDTO>> findBettingOrderHistory(Long bettingId) {
        LocalDateTime startDateTime = null;
        try{
            startDateTime = bettingQueryRepository.findById(bettingId).get().getCreatedAt();
        }catch (Exception e){
            log.error("findBettingOrderHistory error", e);
            throw new RuntimeException("{error: not found betting id}");
        }
        List<LocalDateTime> timeSlots = createFiveMinuteTimeSlots(startDateTime);
        // List<BettingOrderStatisticsDTO> bettingOrders = bettingOrderQueryRepository.findBettingOrderHistory(bettingId);
        List<BettingOrderStatisticsDTO> bettingOrders = bettingOrderStatistics.findBettingOrderHistory(bettingId);
        Map<Long, List<BettingOrderStatisticsDTO>> organizedOrders = organizeOptions(bettingOrders);
        if (organizedOrders.isEmpty()) {
            return initBettingData(timeSlots, bettingId);
        }
        Map<Long, List<BettingOrderStatisticsDTO>> result = fillMissingTimeDataInMap(organizedOrders, timeSlots);
        calculateAndSetRatios(result);
        return result;
    }

    /**
     * 주문 데이터가 없을 경우 초기화된 데이터를 반환합니다
     * @param timeSlots 시간 슬롯
     * @param bettingId 배팅 ID
     * @return 초기화된 주문 데이터
     */
    private Map<Long, List<BettingOrderStatisticsDTO>> initBettingData(List<LocalDateTime> timeSlots, Long bettingId) {
        // List<BettingOption>
        List<BettingOptionDTO> arr = bettingOptionQueryRepository.findByBettingId(bettingId);
        Map<Long, List<BettingOrderStatisticsDTO>> result = new HashMap<>();
        int ratio = 100 / arr.size();
        for (BettingOptionDTO bettingOptionDTO : arr) {
            List<BettingOrderStatisticsDTO> list = new ArrayList<>();
            for (LocalDateTime timeSlot : timeSlots) {
                BettingOrderStatisticsDTO bettingOrderStatisticsDTO = new BettingOrderStatisticsDTO();
                bettingOrderStatisticsDTO.setBettingOptionId(bettingOptionDTO.getOptionId());
                bettingOrderStatisticsDTO.setOrderDate(timeSlot.toLocalDate());
                bettingOrderStatisticsDTO.setOrderTime(timeSlot.toLocalTime());
                bettingOrderStatisticsDTO.setTotalPoints(BigDecimal.ZERO);
                bettingOrderStatisticsDTO.setRatio(ratio);
                list.add(bettingOrderStatisticsDTO);
            }
            result.put(bettingOptionDTO.getOptionId(), list);
        }
        return result;
    }

    /**
     * 각 옵션별로 비율을 계산하여 설정합니다
     * @param bettingOrderMap 옵션별 주문 데이터
     * @return 비율이 계산된 주문 데이터
     */
    public void calculateAndSetRatios(Map<Long, List<BettingOrderStatisticsDTO>> bettingOrderMap) {
        if (bettingOrderMap.isEmpty()) return;

        // Map의 키 개수를 가져옴
        int numberOfKeys = bettingOrderMap.size();

        // 리스트 길이가 동일하다고 가정
        int listSize = bettingOrderMap.values().iterator().next().size();

        // 각 리스트의 동일 인덱스 항목들끼리 비교
        for (int i = 0; i < listSize; i++) {
            // 각 인덱스에서 totalPoints 합산
            BigDecimal totalPointsSum = BigDecimal.ZERO;
            for (List<BettingOrderStatisticsDTO> list : bettingOrderMap.values()) {
                totalPointsSum = totalPointsSum.add(list.get(i).getTotalPoints());
            }

            // totalPointsSum이 0이 아닐 때 비율 계산
            if (totalPointsSum.compareTo(BigDecimal.ZERO) != 0) {
                for (List<BettingOrderStatisticsDTO> list : bettingOrderMap.values()) {
                    BettingOrderStatisticsDTO currentDto = list.get(i);
                    // 비율을 BigDecimal로 계산하고 int로 변환하여 설정
                    BigDecimal ratioDecimal = currentDto.getTotalPoints().divide(totalPointsSum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    int ratio = ratioDecimal.intValue(); // 비율을 int로 변환
                    currentDto.setRatio(ratio);
                }
            } else {
                // totalPointsSum이 0인 경우, 각 항목에 대해 비율을 100 / 키의 개수로 설정
                int equalRatio = 100 / numberOfKeys;
                for (List<BettingOrderStatisticsDTO> list : bettingOrderMap.values()) {
                    BettingOrderStatisticsDTO currentDto = list.get(i);
                    currentDto.setRatio(equalRatio); // 각 항목의 비율을 동일하게 나눔
                }
            }
        }
    }


    private Map<Long, List<BettingOrderStatisticsDTO>> fillMissingTimeDataInMap(
            Map<Long, List<BettingOrderStatisticsDTO>> orders,
            List<LocalDateTime> dayTimeSlots) {

        // 결과를 담을 Map 생성
        Map<Long, List<BettingOrderStatisticsDTO>> result = new HashMap<>();

        // 각 옵션별로 데이터를 순회
        for (Map.Entry<Long, List<BettingOrderStatisticsDTO>> entry : orders.entrySet()) {
            Long bettingOptionId = entry.getKey();
            List<BettingOrderStatisticsDTO> orderStatisticsList = entry.getValue();

            // 결과에 추가할 리스트 생성
            List<BettingOrderStatisticsDTO> filledStatisticsList = new ArrayList<>();

            // 이전 totalPoints를 추적할 변수
            BigDecimal previousTotalPoints = BigDecimal.ZERO;
            int index = 0;

            // dayTimeSlots 순회
            for (LocalDateTime timeSlot : dayTimeSlots) {

                if (index == 0){
                    // 첫번째 슬롯에 데이터가 없으면 100 / 옵션의 개수 으로 채움
                    BettingOrderStatisticsDTO currentOrder = orderStatisticsList.get(index);
                    currentOrder.setRatio(100 / orders.entrySet().size());
                    index++;
                    continue;
                }
                // 현재 슬롯에 맞는 데이터가 있는지 확인
                if (index < orderStatisticsList.size()) {
                    BettingOrderStatisticsDTO currentOrder = orderStatisticsList.get(index);
                    LocalDateTime currentDateTime = LocalDateTime.of(currentOrder.getOrderDate(), currentOrder.getOrderTime());

                    // 슬롯과 일치하는 시간이 있으면 추가하고 인덱스 증가
                    if (currentDateTime.equals(timeSlot)) {
                        BigDecimal sum = currentOrder.getTotalPoints().add(previousTotalPoints);
                        currentOrder.setTotalPoints(sum);
                        filledStatisticsList.add(currentOrder);
                        previousTotalPoints = sum;
                        index++;  // 일치하는 데이터가 있는 경우 인덱스를 증가시킴
                        continue;
                    }
                }

                // 일치하는 시간이 없으면 새로운 DTO 생성하여 이전 totalPoints 또는 0으로 채움
                BettingOrderStatisticsDTO missingOrder = new BettingOrderStatisticsDTO();
                missingOrder.setBettingOptionId(bettingOptionId);
                missingOrder.setOrderDate(timeSlot.toLocalDate());
                missingOrder.setOrderTime(timeSlot.toLocalTime());
                missingOrder.setTotalPoints(previousTotalPoints); // 이전 totalPoints를 설정

                filledStatisticsList.add(missingOrder);
            }

            // 결과에 채워진 리스트 추가
            result.put(bettingOptionId, filledStatisticsList);
        }

        return result;
    }


    /**
     * 주문 데이터를 옵션별로 정리하여 반환합니다
     * @param bettingOrders 주문 데이터
     * @return 옵션별 주문 데이터
     */
    private Map<Long, List<BettingOrderStatisticsDTO>> organizeOptions(List<BettingOrderStatisticsDTO> bettingOrders) {
        Map<Long, List<BettingOrderStatisticsDTO>> result = new HashMap<>();

        for (BettingOrderStatisticsDTO bettingOrder : bettingOrders) {
            // 키가 존재하면 해당 리스트에 추가, 없으면 새 리스트를 만들어 추가
            result.computeIfAbsent(bettingOrder.getBettingOptionId(), k -> new ArrayList<>()).add(bettingOrder);
        }
        return result;
    }

    /**
     * 특정 시간을 기반으로 현재 시간까지 5분간격으로 생성된 리스트를 반환합니다
     * @param startDateTime 시작 시간
     * @return 5분 단위로 생성된 시간 리스트
     */
    public List<LocalDateTime> createFiveMinuteTimeSlots(LocalDateTime startDateTime) {
        // 현재 시간 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now().withNano(0);

        // 5분 단위로 생성된 시간을 담을 리스트
        List<LocalDateTime> timeIntervals = new ArrayList<>();

        startDateTime = startDateTime.truncatedTo(ChronoUnit.MINUTES).minusMinutes(startDateTime.getMinute() % 5);

        // 5분씩 증가하며 리스트에 추가
        while (!startDateTime.isAfter(currentDateTime)) {
            timeIntervals.add(startDateTime);
            startDateTime = startDateTime.plusMinutes(5);
        }

        if (!startDateTime.isEqual(currentDateTime)) {
            timeIntervals.add(currentDateTime);
        }

        return timeIntervals;
    }

    public Map<Long, Map<LocalDate, List<StatisticsTimeDTO>>> findBettingOrderHistoryInLastHour(Long bettingId) {
        return bettingOrderStatistics.findBettingOrderHistoryInLastHour(bettingId);
    }
}
