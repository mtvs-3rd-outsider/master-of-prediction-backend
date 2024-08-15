package com.outsider.masterofpredictionbackend.bettingorder.command;

import com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request.BettingOrderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.command.application.service.BettingOrderCommandService;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.BettingProductValidator;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.UserPoint;
import com.outsider.masterofpredictionbackend.bettingorder.command.infrastructure.UserPointImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;


public class BettingOrderCommandServiceTest {

    @InjectMocks
    private BettingOrderCommandService bettingOrderCommandService;

    @Mock
    private BettingProductValidator bettingProductValidator;

    @Mock
    private UserPoint userPoint;

    @InjectMocks
    private UserPointImpl userPointImpl;

    /*
        BettingOrderCommandService 테스트 코드 작성
        1. point 음수 넣기
        2. 더미 유저를 불러와서 point 비교
     */

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 객체 초기화
    }

    static Stream<Arguments> validatePointTest() {
        return Stream.of(
            Arguments.of(createBettingOrderDTO(1L, 1L, BigDecimal.ZERO, 1L, LocalDate.now(), LocalTime.now()))
        );
    }

    static Stream<Arguments> validateBettingProductStatusTest() {
        return Stream.of(
            Arguments.of(createBettingOrderDTO(1L, 1L, BigDecimal.valueOf(10L), 1L, LocalDate.now(), LocalTime.now()))
        );
    }

    @ParameterizedTest
    @MethodSource("validatePointTest")
    @DisplayName("포인트 유효성 검사")
    void validatePointTest(BettingOrderDTO bettingOrderDTO) {

        if (bettingOrderDTO.getPoint().compareTo(BigDecimal.ZERO) <= 0) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                bettingOrderCommandService.buyBettingProduct(bettingOrderDTO);
            });
            return;
        }
        when(userPoint.find(bettingOrderDTO.getUserId())).thenReturn(BigDecimal.valueOf(-1L));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bettingOrderCommandService.buyBettingProduct(bettingOrderDTO);
        });
    }

    @ParameterizedTest
    @MethodSource("validateBettingProductStatusTest")
    @DisplayName("배팅 상품 유효성 검사")
    void validateBettingProductStatusTest(BettingOrderDTO bettingOrderDTO) {

        when(userPoint.find(bettingOrderDTO.getUserId())).thenReturn(BigDecimal.valueOf(10L));
        when(bettingProductValidator.validateProductExistenceAndStatus(bettingOrderDTO.getBettingId())).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bettingOrderCommandService.buyBettingProduct(bettingOrderDTO);
        });
    }

    private static BettingOrderDTO createBettingOrderDTO(
        Long userId,
        Long bettingId,
        BigDecimal point,
        Long bettingOptionId,
        LocalDate orderDate,
        LocalTime orderTime
    ){
        BettingOrderDTO bettingOrderDTO = new BettingOrderDTO();
        bettingOrderDTO.setUserId(userId);
        bettingOrderDTO.setBettingId(bettingId);
        bettingOrderDTO.setPoint(point);
        bettingOrderDTO.setBettingOptionId(bettingOptionId);
        bettingOrderDTO.setOrderDate(orderDate);
        bettingOrderDTO.setOrderTime(orderTime);
        return bettingOrderDTO;
    }
}
