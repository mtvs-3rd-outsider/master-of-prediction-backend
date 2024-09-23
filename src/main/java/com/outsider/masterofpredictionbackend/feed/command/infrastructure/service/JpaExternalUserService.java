package com.outsider.masterofpredictionbackend.feed.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoResponseDTO;
import com.outsider.masterofpredictionbackend.user.query.application.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaExternalUserService implements ExternalUserService {
    public final UserInfoService userInfoService;

    public JpaExternalUserService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }
    @Override
    public UserDTO getUser(String userId) {
        Optional<UserInfoResponseDTO> optionalDTO = userInfoService.getUserInfoById(Long.parseLong(userId));
        UserInfoResponseDTO dto = optionalDTO.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(dto.getId());
        userDTO.setUserImg(dto.getAvatarUrl());
        userDTO.setUserName(dto.getUserName());
        //티어넣어야함
        userDTO.setDisplayName(dto.getDisplayName());
        //권한다름
        return userDTO;
    }
}
