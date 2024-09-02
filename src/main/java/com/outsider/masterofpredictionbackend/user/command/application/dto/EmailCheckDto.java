package com.outsider.masterofpredictionbackend.user.command.application.dto;


import lombok.Data;

@Data
public class EmailCheckDto {

    private String email;
    private String authNum;
}