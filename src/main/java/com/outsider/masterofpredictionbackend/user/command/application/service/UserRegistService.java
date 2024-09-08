package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.ProviderInfo;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.domain.service.MyChanneRegistClient;
import com.outsider.masterofpredictionbackend.user.command.domain.service.MyChannelInfoUpdateClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserRegistService {

    private final UserCommandRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MyChanneRegistClient myChanneRegistClient;
    public UserRegistService(UserCommandRepository userRepository, BCryptPasswordEncoder passwordEncoder, BCryptPasswordEncoder passwordEncoder1, MyChanneRegistClient myChanneRegistClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder1;
        this.myChanneRegistClient = myChanneRegistClient;
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
        Long userId=  userRepository.save(newUser).getId();
        myChanneRegistClient.publish(userId);
        return userId;

    }
    /*
     사용자 등록
     */
    @Transactional
    public Long registUser(SignUpRequestDTO userRegistRequestDTO) {
       return registUser(new UserRegistDTO(userRegistRequestDTO,Authority.ROLE_USER));
    }
}
