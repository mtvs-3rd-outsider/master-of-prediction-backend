package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductState;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.betting.command.infrastructure.service.BettingOrderSumPointDTO;
import com.outsider.masterofpredictionbackend.betting.command.infrastructure.service.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class BettingSettlementExceptionTest {


    @Mock
    private BettingProductRepository bettingRepository;

    @Mock
    private BettingOrderService bettingOrderService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BettingProductService bettingProductService;

    private BettingProduct mockBettingProduct;
    private final Long userId = 1L;
    private final Long userId2 = 2L;
    private final Long productId = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화


        BettingProduct mockBettingProduct = new BettingProduct(
                "title",
                "content",
                userId,
                1, // categoryCode
                LocalDate.now().minusDays(1), // 어제 날짜로 마감일자를 설정하여 마감일이 지났음을 시뮬레이션
                LocalTime.now().minusHours(1), // 현재 시간보다 이전 시간으로 설정
                false,
                null
        );
    }


    @Test
    @Tag("throws exception test")
    @DisplayName("배팅이 없는 상품을 정산하고자 할 때 예외 발생")
    public void bettingProductNullExceptionTest() {

        when(bettingRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            bettingProductService.settlementBettingProduct(productId, 1L, 1L);
        });

    }

    @Test
    @Tag("throws exception test")
    @DisplayName("배팅 상품 생성자와 요청자가 다를 때 예외 발생")
    public void bettingProductCreateUserEqualsRequestUserExceptionTest() {

        when(bettingRepository.findById(productId)).thenReturn(Optional.of(
                mockBettingProduct
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bettingProductService.settlementBettingProduct(productId, userId2, 1L);
        });
        Assertions.assertEquals("Does not match the product registrant", exception.getMessage());
    }

    @Test
    @Tag("throws exception test")
    @DisplayName("배팅 상품이 이미 정산된 경우 예외 발생")
    public void bettingProductStateIsNotProgressExceptionTest() {

        when(bettingRepository.findById(productId)).thenReturn(Optional.of(
                mockBettingProduct
        ));

        mockBettingProduct.setState(BettingProductState.SETTLING);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bettingProductService.settlementBettingProduct(productId, userId, 1L);
        });
        Assertions.assertEquals("betting product is not settle state", exception.getMessage());
    }

    @Test
    @Tag("throws exception test")
    @DisplayName("마감일자가 지나지 않는 배팅 상품을 정산하고자 할 때 예외 발생")
    public void bettingDeadLineExceptionTest() {

        BettingProduct bettingProduct = new BettingProduct(
                "title",
                "content",
                userId,
                1, // categoryCode
                LocalDate.now().plusMonths(1), // 어제 날짜로 마감일자를 설정하여 마감일이 지났음을 시뮬레이션
                LocalTime.now().minusHours(1), // 현재 시간보다 이전 시간으로 설정
                false,
                null
        );
        BettingProduct bettingProduct2 = new BettingProduct(
                "title",
                "content",
                userId,
                1, // categoryCode
                LocalDate.now(), // 어제 날짜로 마감일자를 설정하여 마감일이 지났음을 시뮬레이션
                LocalTime.now().plusMinutes(10), // 현재 시간보다 이전 시간으로 설정
                false,
                null
        );

        when(bettingRepository.findById(productId)).thenReturn(Optional.of(
                bettingProduct
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bettingProductService.settlementBettingProduct(productId, userId, 1L);
        });
        Assertions.assertEquals("betting product deadline is not passed", exception.getMessage());

        when(bettingRepository.findById(productId)).thenReturn(Optional.of(
                bettingProduct2
        ));

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            bettingProductService.settlementBettingProduct(productId, userId, 1L);
        });
        Assertions.assertEquals("betting product deadline is not passed", exception2.getMessage());
    }
}
