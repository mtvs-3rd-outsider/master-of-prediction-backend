package com.outsider.masterofpredictionbackend.user.query.application.service.mapper;

import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoResponseDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoResponseDTO userToUserInfoDTO(User user);
}