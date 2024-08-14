package com.outsider.masterofpredictionbackend.user.command.application.dto;


import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import lombok.Data;

@Data
public class CustomUserInfoDTO {
    private Long userId;
    private String email;
    private String role;

    public CustomUserInfoDTO(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.role = user.getAuthority();
    }
}
