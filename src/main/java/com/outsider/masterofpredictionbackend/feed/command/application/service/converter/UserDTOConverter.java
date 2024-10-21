package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter {

    public User toEntity(UserDTO dto) {
        return new User(
                dto.getUserId()
        );
    }


    public UserDTO fromEntity(User entity) {
        return null;
    }

}
