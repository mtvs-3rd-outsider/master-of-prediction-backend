package com.outsider.masterofpredictionbackend.user.command.application.dto;

import lombok.*;

@Data
public class EmailAuthDTO {
    private String code;
    private Boolean flag;
}
