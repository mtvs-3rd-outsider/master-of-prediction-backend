package com.outsider.masterofpredictionbackend.user.command.application.dto;

import lombok.*;

@Setter
@Getter
@ToString

public class EmailAuthDto {
    private String code;
    private Boolean flag;

    public EmailAuthDto() {
    }

    public EmailAuthDto(Boolean flag, String code) {
        this.flag = flag;
        this.code = code;
    }
}