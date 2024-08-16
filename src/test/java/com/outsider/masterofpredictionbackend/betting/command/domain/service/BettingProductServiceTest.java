package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class BettingProductServiceTest {

    @Autowired
    private BettingProductService bettingProductService;

    @Autowired
    private BettingProductRepository bettingProductRepository;


    static Stream<Arguments> bettingProduct() {
        return Stream.of(
                Arguments.of(new BettingProduct(
                        "title",
                        "content",
                        1,
                        1,
                        LocalDate.now(),
                        LocalTime.now(),
                        false
                ), List.of(new BettingProductImage(
                            1,
                            "test"
                        )), List.of(new BettingProductOption(
                                    1,
                                    "option",
                                    "test"
                        ), new BettingProductOption(
                                1,
                                "option",
                                "test"
                        ))),
                Arguments.of(new BettingProduct(
                        "title",
                        "content",
                        1,
                        1,
                        LocalDate.now(),
                        LocalTime.now(),
                        false
                ), List.of(new BettingProductImage(
                        1,
                        "test"
                        )), List.of(new BettingProductOption(
                                    1,
                                    "option",
                                    "test"
                        ), new BettingProductOption(
                                1,
                                "option",
                                "test"
                        )))

        );
    }

    @DisplayName("배팅 등록 테스트")
    @MethodSource("bettingProduct")
    @ParameterizedTest
    @Transactional
    void saveTest( BettingProduct bettingProduct,
                   List<BettingProductImage> bettingProductImages,
                   List<BettingProductOption> bettingProductOptions){
        if (bettingProductOptions.size() < 2){
            Assertions.assertThrows(IllegalArgumentException.class,() ->
                    bettingProductService.save(bettingProduct, bettingProductImages, bettingProductOptions));
        } else{
            bettingProductService.save(bettingProduct, bettingProductImages, bettingProductOptions);
            Assertions.assertNotNull(bettingProductService.findById(bettingProduct.getId()));
        }
    }

    @DisplayName("배팅 상품 주문 검증 테스트")
    @Transactional
    @Test
    void validateProductExistenceAndStatusTest() {
        BettingProduct bettingProduct = new BettingProduct(
                "title",
                "content",
                1,
                1,
                LocalDate.now(),
                LocalTime.now().minusHours(1),
                false
        );
        BettingProduct bettingProduct2 = new BettingProduct(
                "title",
                "content",
                1,
                1,
                LocalDate.now().plusMonths(1),
                LocalTime.now().plusHours(1),
                false
        );

        bettingProductRepository.save(bettingProduct);
        bettingProductRepository.save(bettingProduct2);


        Assertions.assertFalse(bettingProductService.validateProductExistenceAndStatus(0L));
        Assertions.assertFalse(bettingProductService.validateProductExistenceAndStatus(bettingProduct.getId()));
        Assertions.assertTrue(bettingProductService.validateProductExistenceAndStatus(bettingProduct2.getId()));
    }
}
