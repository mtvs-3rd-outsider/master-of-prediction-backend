package com.outsider.masterofpredictionbackend.betting.command.application.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductOptionRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingProductService;
import com.outsider.masterofpredictionbackend.betting.command.infrastructure.service.UserRepository;
import com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request.BettingOrderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.command.application.service.BettingOrderCommandService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import com.outsider.masterofpredictionbackend.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;

@SpringBootTest
@Transactional
public class BettingSettlementTest {

    @Autowired
    private BettingProductService bettingProductService;

    @Autowired
    private BettingProductRepository bettingProductRepository;

    @Autowired
    private BettingProductOptionRepository bettingProductOptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BettingOrderCommandService bettingOrderCommandService;

    @Test
    @DisplayName("배팅 정산 테스트")
    public void bettingSettlementTest() {
        try (MockedStatic<LocalDate> mockedLocalDate = mockStatic(LocalDate.class);
             MockedStatic<LocalTime> mockedLocalTime = mockStatic(LocalTime.class)) {
            LocalDate mockDate = LocalDate.of(2024, 10, 17);
            LocalTime mockTime = LocalTime.of(12, 0, 0);

            // LocalDate.now()가 호출될 때 mockDate를 반환하도록 모킹
            mockedLocalDate.when(LocalDate::now).thenReturn(mockDate);

            // LocalTime.now()가 호출될 때 mockTime을 반환하도록 모킹
            mockedLocalTime.when(LocalTime::now).thenReturn(mockTime);


            User user = new User(
                    "test@testUser.com",
                    "1234",
                    "testUser",
                    "testUser",
                    20,
                    Gender.FEMALE,
                    Location.KOREA,
                    Authority.ROLE_USER
            );
            user.setPoints(BigDecimal.valueOf(3000));
            User user2 = new User(
                    "test2@testUser.com",
                    "1234",
                    "testUser2",
                    "testUser2",
                    20,
                    Gender.FEMALE,
                    Location.KOREA,
                    Authority.ROLE_USER
            );
            user2.setPoints(BigDecimal.valueOf(3000));

            user.setId(IdGenerator.generateId());
            user2.setId(IdGenerator.generateId());
            userRepository.save(user);
            userRepository.save(user2);


            System.out.println("LocalDate.now() = " + LocalDate.now());
            System.out.println("LocalDate.now().getYear = " + LocalDate.now().getYear());
            System.out.println("LocalTime.now() = " + LocalTime.now());

            BettingProduct bettingProduct = new BettingProduct(
                    "title",
                    "content",
                    user.getId(),
                    1,
                    LocalDate.now(),
                    LocalTime.now().plusSeconds(2),
                    false,
                    "test"
            );


            bettingProductRepository.save(bettingProduct);


            BettingProductOption bettingProductOption = new BettingProductOption(
                    bettingProduct.getId(),
                    "option1",
                    "content"
            );
            BettingProductOption bettingProductOption2 = new BettingProductOption(
                    bettingProduct.getId(),
                    "option2",
                    "content"
            );

            bettingProductOptionRepository.save(bettingProductOption);
            bettingProductOptionRepository.save(bettingProductOption2);

            BettingOrderDTO bettingOrderDTO = new BettingOrderDTO(
                    user.getId(),
                    bettingProduct.getId(),
                    BigDecimal.valueOf(1000),
                    bettingProductOption.getId()
            );

            BettingOrderDTO bettingOrderDTO2 = new BettingOrderDTO(
                    user2.getId(),
                    bettingProduct.getId(),
                    BigDecimal.valueOf(1000),
                    bettingProductOption2.getId()
            );


            mockedLocalTime.when(LocalTime::now).thenReturn(LocalTime.of(12, 0, 1));


            bettingOrderCommandService.buyBettingProduct(bettingOrderDTO);
            bettingOrderCommandService.buyBettingProduct(bettingOrderDTO2);

            mockedLocalTime.when(LocalTime::now).thenReturn(LocalTime.of(12, 0, 3));
            bettingProductService.settlementBettingProduct(bettingProduct.getId(), user.getId(), bettingOrderDTO.getBettingOptionId());


            User result = userRepository.findById(user.getId()).orElse(null);
            System.out.println(result.getPoints());
        }

    }


}
