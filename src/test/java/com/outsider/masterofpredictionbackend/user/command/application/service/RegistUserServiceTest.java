package com.outsider.masterofpredictionbackend.user.command.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserInfoRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Transactional
class RegistUserServiceTest {
    @Autowired
    private RegistUserService registUserService;

    @Autowired
    private UserCommandRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegistUser_Success() {
        // given
        UserInfoRequestDTO dto = new UserInfoRequestDTO(
                "test@example.com",
                "password123",
                "TestuserName",
                25,
                Gender.MALE,
                Location.KOREA,
                Authority.ROLE_USER
        );

        // when
        registUserService.registUser(dto);

        // then
        Optional<User> savedUser = userRepository.findByEmail(dto.getEmail());
        assertTrue(savedUser.isPresent());
        assertEquals(dto.getEmail(), savedUser.get().getEmail());
        assertEquals(dto.getUserName(), savedUser.get().getUserName());
    }

    @Test
    public void testRegistUser_DuplicateEmail() {
        // given
        UserInfoRequestDTO dto1 = new UserInfoRequestDTO(
                "duplicate@example.com",
                "password123",
                "userName1",
                30,
                Gender.FEMALE,
                Location.KOREA,
                Authority.ROLE_USER
        );
        registUserService.registUser(dto1);

        UserInfoRequestDTO dto2 = new UserInfoRequestDTO(
                "duplicate@example.com",
                "password456",
                "userName2",
                28,
                Gender.MALE,
                Location.USA,
                Authority.ROLE_USER

        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registUserService.registUser(dto2);
        });

        assertEquals("User with this ID already exists", exception.getMessage());
    }
}