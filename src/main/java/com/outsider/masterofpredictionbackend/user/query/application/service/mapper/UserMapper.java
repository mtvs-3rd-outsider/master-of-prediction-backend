package com.outsider.masterofpredictionbackend.user.query.application.service.mapper;

import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoResponseDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "avatarUrl", source = "userImg")
    @Mapping(target = "tier.name", source = "tierName")
    @Mapping(target = "tier.level", source = "tierLevel")
    UserInfoResponseDTO userToUserInfoDTO(User user);
}