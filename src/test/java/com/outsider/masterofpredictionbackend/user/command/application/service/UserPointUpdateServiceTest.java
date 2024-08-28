package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserPointUpdateDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserPointUpdateServiceTest {

    @Autowired
    private UserPointUpdateService userPointUpdateService;
    @Autowired
    private UserCommandRepository userCommandRepository;
    @Autowired
    private RegistUserService registUserService;

    private Long existingUser;

    @BeforeEach
    public void setUp() {
        // 기존 사용자 생성 및 저장
        UserRegistDTO dto = new UserRegistDTO(
                "test@example.com",
                "password123",
                "TestuserName",
                Authority.ROLE_USER
        );

        existingUser = registUserService.registUser(dto);
    }

    @Test
    public void testIncreaseUserPoints() {
        BigDecimal initialPoints = new BigDecimal("100.00");
        BigDecimal pointsToAdd = new BigDecimal("50.50");

        // 초기 포인트 설정
        User user = userCommandRepository.findById(existingUser).orElseThrow();
        user.setPoints(initialPoints);
        userCommandRepository.save(user);

        // 포인트 증가
        userPointUpdateService.increaseUserPoints(new UserPointUpdateDTO(existingUser, pointsToAdd));

        // 포인트 증가 확인
        User updatedUser = userCommandRepository.findById(existingUser).orElseThrow();
        assertEquals(initialPoints.add(pointsToAdd), updatedUser.getPoints());
    }

    @Test
    public void testDecreaseUserPoints() {
        BigDecimal initialPoints = new BigDecimal("100.00");
        BigDecimal pointsToSubtract = new BigDecimal("30.30");

        // 초기 포인트 설정
        User user = userCommandRepository.findById(existingUser).orElseThrow();
        user.setPoints(initialPoints);
        userCommandRepository.save(user);

        // 포인트 감소
        userPointUpdateService.decreaseUserPoints(new UserPointUpdateDTO(existingUser, pointsToSubtract));

        // 포인트 감소 확인
        User updatedUser = userCommandRepository.findById(existingUser).orElseThrow();
        assertEquals(initialPoints.subtract(pointsToSubtract), updatedUser.getPoints());
    }

    @Test
    public void testDecreaseUserPoints_InsufficientPoints() {
        BigDecimal initialPoints = new BigDecimal("20.00");
        BigDecimal pointsToSubtract = new BigDecimal("30.00");

        // 초기 포인트 설정
        User user = userCommandRepository.findById(existingUser).orElseThrow();
        user.setPoints(initialPoints);
        userCommandRepository.save(user);

        // 포인트가 부족한 상황에서 감소 시도
        assertThrows(IllegalArgumentException.class, () -> {
            userPointUpdateService.decreaseUserPoints(new UserPointUpdateDTO(existingUser, pointsToSubtract));
        });
    }
}
