package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserInfoRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistUserService {

    private final UserCommandRepository userRepository;

    @Autowired
    public RegistUserService(UserCommandRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long registUser(UserInfoRequestDTO userRegistRequestDTO) {
        // 중복된 사용자가 있는지 확인
        if (userRepository.findByEmail(userRegistRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this ID already exists");
        }
        // 새로운 사용자 생성
        User newUser = new User(
                userRegistRequestDTO.getEmail(),
                userRegistRequestDTO.getPassword(),
                userRegistRequestDTO.getNickName(),
                userRegistRequestDTO.getAge(),
                userRegistRequestDTO.getGender(),
                userRegistRequestDTO.getLocation()
        );
        // 사용자 저장
        return userRepository.save(newUser).getId();
    }
}
