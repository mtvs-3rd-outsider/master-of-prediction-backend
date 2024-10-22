package com.outsider.masterofpredictionbackend.user.command.application.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckDto {

    private String email;
    private String authNum;
}