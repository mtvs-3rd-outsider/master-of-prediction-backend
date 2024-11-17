package com.outsider.masterofpredictionbackend.user.command.application.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    // Getters and Setters
}