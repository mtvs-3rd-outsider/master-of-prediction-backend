package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserInfoRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Location;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UpdateUserServiceTest {

    @Autowired
    private UpdateUserService updateUserService;
    @Autowired
    private RegistUserService registUserService;


    private Long existingUser;
    @BeforeEach
    public void setUp() {
        // 기존 사용자 생성 및 저장
        UserInfoRequestDTO dto = new UserInfoRequestDTO(
                "test@example.com",
                "password123",
                "TestNickName",
                25,
                Gender.MALE,
                Location.KOREA
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
        updateDTO.setNickName("newNickName");
        updateDTO.setAge(31);
        updateDTO.setGender(Gender.FEMALE);
        updateDTO.setLocation(Location.USA);

        // 업데이트 수행
        User updatedUser = updateUserService.updateUser(updateDTO);

        // 업데이트된 사용자 정보 확인
        assertNotNull(updatedUser);
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals("newNickName", updatedUser.getNickName());
        assertEquals(31, updatedUser.getAge());
        assertEquals(Gender.FEMALE, updatedUser.getGender());
        assertEquals(Location.USA, updatedUser.getLocation());
    }

    @Test
    public void testUpdateUser_PartialUpdate() {
        // 일부 필드만 업데이트할 데이터 준비
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO();
        updateDTO.setUserId(existingUser);
        updateDTO.setNickName("newNickName");

        // 업데이트 수행
        User updatedUser = updateUserService.updateUser(updateDTO);

        // 업데이트된 사용자 정보 확인
        assertNotNull(updatedUser);
        assertEquals("password123", updatedUser.getPassword()); // 기존 비밀번호 유지
        assertEquals("newNickName", updatedUser.getNickName());  // 닉네임만 변경됨
        assertEquals(25, updatedUser.getAge());                  // 기존 나이 유지
        assertEquals(Gender.MALE, updatedUser.getGender());           // 기존 성별 유지
        assertEquals(Location.KOREA, updatedUser.getLocation());        // 기존 위치 유지
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