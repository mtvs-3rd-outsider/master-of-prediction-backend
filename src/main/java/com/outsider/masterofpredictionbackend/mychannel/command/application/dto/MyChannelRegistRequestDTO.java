package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class MyChannelRegistRequestDTO {

    private String displayName;

    private String bio;

    private String location;

    private LocalDate joinDate;

    private String website;

    private Long user;

}
