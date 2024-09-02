package com.outsider.masterofpredictionbackend.user.command.application.dto;

import lombok.*;

@Data
public class EmailAuthDTO {
    private String code;
    private Boolean flag;

    public EmailAuthDTO(String code, Boolean flag) {
        this.code = code;
        this.flag = flag;
    }
}
