package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DeleteUserServiceTest {

    @Autowired
    private DeleteUserService deleteUserService;
    @Autowired
    private UserCommandRepository userCommandRepository;
    @Autowired
    private UserRegistService registUserService;

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

        // when
        existingUser = registUserService.registUser(dto);
    }

    @Test
    public void testDeleteUser_Success() {
        // 사용자 삭제 수행 (실제로는 isWithdrawal 플래그 설정)
        deleteUserService.deleteUser(existingUser);

        // 사용자 정보 가져오기
        User user = userCommandRepository.findById(existingUser)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // isWithdrawal 플래그가 true로 설정되었는지 확인
        assertTrue(user.getWithdrawal());
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // 존재하지 않는 사용자 ID로 삭제 시도
        Long nonExistentUserId = 999L;

        // 예외가 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            deleteUserService.deleteUser(nonExistentUserId);
        });
    }
}
