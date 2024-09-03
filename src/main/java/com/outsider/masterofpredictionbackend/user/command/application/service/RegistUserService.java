package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.ProviderInfo;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class RegistUserService {

    private final UserCommandRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistUserService(UserCommandRepository userRepository, BCryptPasswordEncoder passwordEncoder, BCryptPasswordEncoder passwordEncoder1) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder1;
    }
    /*
     사용자 등록
     */
    @Transactional
    public Long registUser(UserRegistDTO userRegistRequestDTO) {
        // 중복된 사용자가 있는지 확인
        if (userRepository.findByEmail(userRegistRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this ID already exists");
        }
        // 새로운 사용자 생성
        User newUser = new User(
                userRegistRequestDTO.getEmail(),
                passwordEncoder.encode(userRegistRequestDTO.getPassword()),
                userRegistRequestDTO.getUserName(),
               userRegistRequestDTO.getAuthority(),
                new ProviderInfo()
        );
        // 사용자 저장

        log.info("사용자 등록됨. id: {}", newUser.getId());
        return userRepository.save(newUser).getId();
    }
    /*
     사용자 등록
     */
    @Transactional
    public Long registUser(SignUpRequestDTO userRegistRequestDTO) {
       return registUser(new UserRegistDTO(userRegistRequestDTO,Authority.ROLE_USER));
    }
}
