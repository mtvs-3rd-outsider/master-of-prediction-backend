package com.outsider.masterofpredictionbackend.user.query.application.service;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.query.CategoryChannelDTO;
import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoResponseDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {

    private final UserCommandRepository userRepository;

    @Autowired
    public UserInfoService(UserCommandRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to retrieve user by ID and map to UserInfoResponseDTO
    public Optional<UserInfoResponseDTO> getUserInfoById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::convertToDTO);
    }

    // Method to retrieve user by email and map to UserInfoResponseDTO
    public Optional<UserInfoResponseDTO> getUserInfoByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(this::convertToDTO);
    }

    // Private method to convert User entity to UserInfoResponseDTO
    private UserInfoResponseDTO convertToDTO(User user) {
        return new UserInfoResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getAge(),
                user.getGender(),
                user.getLocation(),
                user.getAuthority(),
                user.getBirthday(),
                user.getUserImg(),
                user.getDisplayName()

        );
    }
}
