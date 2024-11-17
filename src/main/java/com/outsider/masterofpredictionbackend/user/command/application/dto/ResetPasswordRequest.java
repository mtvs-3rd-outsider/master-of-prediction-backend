package com.outsider.masterofpredictionbackend.user.command.application.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String newPassword;

    // getters and setters
}
