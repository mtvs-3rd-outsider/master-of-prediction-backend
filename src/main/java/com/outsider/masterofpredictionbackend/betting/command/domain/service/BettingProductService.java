package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductState;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductImageRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductOptionRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.infrastructure.service.BettingOrderSumPointDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class BettingProductService {

    private final BettingProductRepository bettingRepository;
    private final BettingProductImageRepository bettingProductImageRepository;
    private final BettingProductOptionRepository bettingProductOptionRepository;
    private final BettingOrderService bettingOrderService;
    private final UserService userService;
    private final BettingKafkaService bettingKafkaService;



    @Autowired
    public BettingProductService(BettingProductRepository bettingRepository, BettingProductImageRepository bettingProductImageRepository, BettingProductOptionRepository bettingProductOptionRepository, BettingOrderService bettingOrderService, UserService userService, KafkaTemplate kafkaTemplate, ObjectMapper objectMapper, BettingKafkaService bettingKafkaService) {
        this.bettingRepository = bettingRepository;
        this.bettingProductImageRepository = bettingProductImageRepository;
        this.bettingProductOptionRepository = bettingProductOptionRepository;
        this.bettingOrderService = bettingOrderService;
        this.userService = userService;
        this.bettingKafkaService = bettingKafkaService;
    }

    public void save(
            List<BettingProductImage> bettingProductImages,
            List<BettingProductOption> bettingProductOptions
    ) {

        if (!bettingProductImages.isEmpty()){
            bettingProductImageRepository.saveAll(bettingProductImages);
        }
        if (bettingProductOptions.isEmpty() || bettingProductOptions.size() < 2){
            throw new IllegalArgumentException("betting option should be more than 2");
        }
        bettingProductOptionRepository.saveAll(bettingProductOptions);
    }

    public BettingProduct findById(Long id) {
        if (id == null) {
            return null;
        }
        return bettingRepository.findById(id).orElse(null);
    }

    public boolean validateProductExistenceAndStatus(Long productId) {
        BettingProduct bettingProduct = findById(productId);
        if (bettingProduct == null) {
            return false;
        }
        if(LocalDate.now().isAfter(bettingProduct.getDeadlineDate())){
            return false;
        }
        return !LocalDate.now().isEqual(bettingProduct.getDeadlineDate()) ||
                !LocalTime.now().isAfter(bettingProduct.getDeadlineTime());
    }

    @Transactional
    public void settlementBettingProduct(Long productId, Long userId, Long optionId) {
        BettingProduct bettingProduct = findById(productId);

        if (bettingProduct == null){
            log.error("betting product id is not exist: {}", productId);
            throw new IllegalArgumentException("betting product is not exist");
        }

        if (!bettingProduct.getUserId().equals(userId)) {
            log.error("Does not match the product registrant userId: {}, bettingProduct.userId: {}", userId, bettingProduct.getUserId());
            throw new IllegalArgumentException("Does not match the product registrant");
        }

        if (bettingProduct.getState() != BettingProductState.PROGRESS) {
            log.error("betting product is not settle state: {}, request user Id: {}", bettingProduct.getState(), userId);
            throw new IllegalArgumentException("betting product is not settle state");
        }

        // NOTE: 정산은 마감일자가 지나야합니다
        if (LocalDate.now().isBefore(bettingProduct.getDeadlineDate()) ||
            (LocalDate.now().isEqual(bettingProduct.getDeadlineDate())
                    && LocalTime.now().isBefore(bettingProduct.getDeadlineTime()))
        ) {
            log.error("betting product deadline is not passed: {}, request user Id: {}", bettingProduct.getDeadlineDate(), userId);
            throw new IllegalArgumentException("betting product deadline is not passed");
        }

        List<BettingOrderSumPointDTO> info = bettingOrderService.calculateUserOrderPointSumByProductId(productId, optionId);
        BigDecimal totalPoint = BigDecimal.ZERO;
        BigDecimal choiceOptionTotalPoint = BigDecimal.ZERO;
        for (BettingOrderSumPointDTO bettingOrderSumPointDTO : info) {
            if (optionId.equals(bettingOrderSumPointDTO.getBettingOptionId())){
                choiceOptionTotalPoint = choiceOptionTotalPoint.add(bettingOrderSumPointDTO.getOrderPoint());
            }
            totalPoint = totalPoint.add(bettingOrderSumPointDTO.getOrderPoint());
        }

        // 내가 선택한 옵션 포인트 / 선택된 옵션의 전체 포인트 = 내가 선택한 옵션의 포인트 비율
        // totalPoint * 내가 선택한 옵션의 포인트 비율
        if (totalPoint.compareTo(BigDecimal.ZERO) != 0){
            for (BettingOrderSumPointDTO bettingOrderSumPointDTO : info) {
                if (bettingOrderSumPointDTO.getBettingOptionId().equals(optionId)){
                    BigDecimal point = bettingOrderSumPointDTO.getOrderPoint()
                            .divide(choiceOptionTotalPoint, 5, RoundingMode.HALF_UP) // 소수점 5자리까지 반올림
                            .multiply(totalPoint);
                    log.info("totalPoint: {}, point: {}", totalPoint, point);
                    userService.pointUpdate(bettingOrderSumPointDTO.getUserId(), point);
                }
            }
        }

        bettingProduct.setState(BettingProductState.SETTLING);
        bettingRepository.save(bettingProduct);

        /*
         * 배팅을 주문한 사용자에게 알림 전송
         */
        List<Long> userIds = bettingOrderService.findUserIdsByProductId(productId);
        List<User> users = userService.findUsersByIds(userIds);
        for (User user : users) {
            log.info("send settlement notification to user: {}", user.getId());
            bettingKafkaService.sendSettlementEvent(users);
        }
    }
}
