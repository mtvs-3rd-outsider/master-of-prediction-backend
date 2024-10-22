package com.outsider.masterofpredictionbackend.user.query.application.service;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.query.CategoryChannelDTO;
import com.outsider.masterofpredictionbackend.exception.NotExistException;
import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoResponseDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.query.application.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {

    private final UserCommandRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserInfoService(UserCommandRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserInfoResponseDTO getUserInfoById(Long id) {
        User user = userRepository.findById(id).orElseThrow(NotExistException::new);
        return userMapper.userToUserInfoDTO(user);
    }

    public UserInfoResponseDTO getUserInfoByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NotExistException::new);
        return userMapper.userToUserInfoDTO(user);
    }

    public Page<UserInfoResponseDTO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        // User 엔티티를 UserInfoResponseDTO로 변환
        return userPage.map(userMapper::userToUserInfoDTO);
    }
}
