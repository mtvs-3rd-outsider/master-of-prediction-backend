package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.ProviderInfo;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.domain.service.MyChanneRegistClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


import java.util.Optional;

@Slf4j
@Service
public class UserRegistService {

    private final UserCommandRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MyChanneRegistClient myChanneRegistClient;

    public UserRegistService(UserCommandRepository userRepository, BCryptPasswordEncoder passwordEncoder, MyChanneRegistClient myChanneRegistClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.myChanneRegistClient = myChanneRegistClient;
    }

    /*
     사용자 등록
     */
    @Transactional
    public Long registUser(UserRegistDTO userRegistRequestDTO) {
        // 중복된 email 확인
        if (userRepository.findByEmail(userRegistRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        // 중복된 user_name 확인 및 처리
        String userName = userRegistRequestDTO.getUserName();
        String newUserName = ensureUniqueUserName(userName);

        // 새로운 사용자 생성
        User newUser = new User(
                userRegistRequestDTO.getEmail(),
                passwordEncoder.encode(userRegistRequestDTO.getPassword()),
                userName,
                newUserName,
                userRegistRequestDTO.getAuthority(),
                userRegistRequestDTO.getProviderInfo()
        );
        // 사용자 저장
        Long userId = userRepository.save(newUser).getId();
        log.info("User registered successfully with ID: {}, userName: {}", userId, userName);
        // 채널 등록 이벤트 발행
        myChanneRegistClient.publish(userId);
        return userId;
    }

    /*
     사용자 등록 (SignUpRequestDTO를 사용하는 경우)
     */
    @Transactional
    public Long registUser(SignUpRequestDTO userRegistRequestDTO) {
        return registUser(new UserRegistDTO(userRegistRequestDTO, Authority.ROLE_USER));
    }

    /*
     고유한 user_name을 보장하는 메서드
     */
    private String ensureUniqueUserName(String userName) {
        Optional<User> existingUser = userRepository.findByUserName(userName);
        if (existingUser.isPresent()) {
            // 중복된 user_name이 존재하면 UUID를 추가하여 고유하게 만듦
            String newUserName = userName + "_" + UUID.randomUUID().toString().substring(0, 8);
            log.warn("UserName {} is already taken, new UserName: {}", userName, newUserName);
            return newUserName;
        }
        return userName;
    }
}
