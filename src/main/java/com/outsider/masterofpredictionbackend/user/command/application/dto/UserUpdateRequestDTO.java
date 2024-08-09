package com.outsider.masterofpredictionbackend.user.command.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Location;
import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private Long userId;
    private String email;
    private String password;
    private String nickName;
    private Integer age;
    private Gender gender;
    private Location location;
}
