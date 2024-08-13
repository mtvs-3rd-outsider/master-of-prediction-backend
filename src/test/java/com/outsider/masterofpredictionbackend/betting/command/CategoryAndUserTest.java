package com.outsider.masterofpredictionbackend.betting.command;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.service.ProductCommandService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CategoryAndUserTest {
    @Autowired
    private ProductCommandService productCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("카테고리 서비스 예외 테스트")
    @Test
    void testCategoryNotFoundBadRequestException() {
        BettingProductAndOptionDTO bettingProductAndOptionDTO = new BettingProductAndOptionDTO();
        bettingProductAndOptionDTO.setCategoryCode(null);
        bettingProductAndOptionDTO.setUserId(1L);

        assertThrows(BadRequestException.class, () -> productCommandService.save(bettingProductAndOptionDTO));
    }

    @DisplayName("유저 서비스 예외 테스트")
    @Test
    void testUserMismatch() {
        BettingProductAndOptionDTO bettingProductAndOptionDTO = new BettingProductAndOptionDTO();
        bettingProductAndOptionDTO.setCategoryCode(1L);
        bettingProductAndOptionDTO.setUserId(null);

        assertThrows(BadRequestException.class, () -> productCommandService.save(bettingProductAndOptionDTO));
    }

}
