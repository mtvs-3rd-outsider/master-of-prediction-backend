package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UpdateUserServiceTest {

    @Autowired
    private UpdateUserService updateUserService;
    @Autowired
    private RegistUserService registUserService;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;


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
    public void testUpdateUser_Success() {
        // 업데이트할 데이터 준비
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO();
        updateDTO.setUserId(existingUser);
        updateDTO.setPassword("newPassword");
        updateDTO.setUserName("newuserName");


        // 업데이트 수행
        User updatedUser = updateUserService.updateUser(updateDTO);

        // 업데이트된 사용자 정보 확인
        assertNotNull(updatedUser);
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals("newuserName", updatedUser.getUserName());

    }

    @Test
    public void testUpdateUser_PartialUpdate() {
        // 일부 필드만 업데이트할 데이터 준비
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO();
        updateDTO.setUserId(existingUser);
        updateDTO.setUserName("newuserName");

        // 업데이트 수행
        User updatedUser = updateUserService.updateUser(updateDTO);

        // 업데이트된 사용자 정보 확인
        assertNotNull(updatedUser);
        assertTrue(passwordEncoder.matches("password123", updatedUser.getPassword())); // 기존 비밀번호 유지
        assertEquals("newuserName", updatedUser.getUserName());  // 닉네임만 변경됨
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // 존재하지 않는 사용자 ID로 업데이트 시도
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO();
        updateDTO.setUserId(999L); // 존재하지 않는 ID

        // 예외가 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            updateUserService.updateUser(updateDTO);
        });
    }
}
