package com.outsider.masterofpredictionbackend.user.query.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoResponseDTO {

    private Long id;
    private String email;
    private String displayName;
    private String userName;
    private Integer age;
    private Gender gender;
    private Location location;
    private Authority authority;
    private  String token;
    private LocalDate birthday;
    private String avatarUrl;
    private Boolean isWithdrawal;

}
