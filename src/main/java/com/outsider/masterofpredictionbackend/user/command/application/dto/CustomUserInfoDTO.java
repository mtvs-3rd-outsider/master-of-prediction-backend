package com.outsider.masterofpredictionbackend.user.command.application.dto;


import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Hidden
@Data
public class CustomUserInfoDTO {
    private Long userId;
    private String email;
    private Authority role;
    private String username;

    public CustomUserInfoDTO() {
    }

    public CustomUserInfoDTO(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.role = user.getAuthority();
        this.username= user.getUserName();
    }
}
