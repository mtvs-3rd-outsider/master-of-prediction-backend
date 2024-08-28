package com.outsider.masterofpredictionbackend.user.query.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoRequestDTO;
import com.outsider.masterofpredictionbackend.user.query.application.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UserInfoServiceTest {

    @Mock
    private UserCommandRepository userRepository;

    @InjectMocks
    private UserInfoService userInfoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserInfoById_Success() {
        // Given
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("user@example.com");
        mockUser.setUserName("John Doe");
        mockUser.setAge(30);
        mockUser.setGender(Gender.MALE);
        mockUser.setLocation(Location.KOREA);
        mockUser.setAuthority(Authority.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // When
        Optional<UserInfoRequestDTO> result = userInfoService.getUserInfoById(userId);

        // Then
        assertTrue(result.isPresent());
        UserInfoRequestDTO dto = result.get();
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("John Doe", dto.getUserName());
        assertEquals(30, dto.getAge());
        assertEquals(Gender.MALE, dto.getGender());
        assertEquals(Location.KOREA, dto.getLocation());
        assertEquals(Authority.ROLE_USER, dto.getAuthority());
    }

    @Test
    public void testGetUserInfoById_NotFound() {
        // Given
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<UserInfoRequestDTO> result = userInfoService.getUserInfoById(userId);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetUserInfoByEmail_Success() {
        // Given
        String email = "user@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setUserName("John Doe");
        mockUser.setAge(30);
        mockUser.setGender(Gender.MALE);
        mockUser.setLocation(Location.KOREA);
        mockUser.setAuthority(Authority.ROLE_USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // When
        Optional<UserInfoRequestDTO> result = userInfoService.getUserInfoByEmail(email);

        // Then
        assertTrue(result.isPresent());
        UserInfoRequestDTO dto = result.get();
        assertEquals(email, dto.getEmail());
        assertEquals("John Doe", dto.getUserName());
        assertEquals(30, dto.getAge());
        assertEquals(Gender.MALE, dto.getGender());
        assertEquals(Location.KOREA, dto.getLocation());
        assertEquals(Authority.ROLE_USER, dto.getAuthority());
    }

    @Test
    public void testGetUserInfoByEmail_NotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<UserInfoRequestDTO> result = userInfoService.getUserInfoByEmail(email);

        // Then
        assertTrue(result.isEmpty());
    }
}
