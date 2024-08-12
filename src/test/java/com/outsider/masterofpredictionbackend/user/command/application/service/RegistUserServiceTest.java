package com.outsider.masterofpredictionbackend.user.command.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserInfoRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Location;
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
                "TestNickName",
                25,
                Gender.MALE,
                Location.KOREA
        );

        // when
        registUserService.registUser(dto);

        // then
        Optional<User> savedUser = userRepository.findByEmail(dto.getEmail());
        assertTrue(savedUser.isPresent());
        assertEquals(dto.getEmail(), savedUser.get().getEmail());
        assertEquals(dto.getNickName(), savedUser.get().getNickName());
    }

    @Test
    public void testRegistUser_DuplicateEmail() {
        // given
        UserInfoRequestDTO dto1 = new UserInfoRequestDTO(
                "duplicate@example.com",
                "password123",
                "NickName1",
                30,
                Gender.FEMALE,
                Location.KOREA
        );
        registUserService.registUser(dto1);

        UserInfoRequestDTO dto2 = new UserInfoRequestDTO(
                "duplicate@example.com",
                "password456",
                "NickName2",
                28,
                Gender.MALE,
                Location.USA
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registUserService.registUser(dto2);
        });

        assertEquals("User with this ID already exists", exception.getMessage());
    }
}