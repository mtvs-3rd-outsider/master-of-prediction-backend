package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.util.DTOConverter;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter implements DTOConverter<UserDTO, User> {
    @Override
    public User toEntity(UserDTO dto) {
        return new User(
                dto.getUserId()
        );
    }

    @Override
    public UserDTO fromEntity(User entity) {
        return null;
    }

    @Override
    public Class<UserDTO> getDtoClass() {
        return UserDTO.class;
    }
}
