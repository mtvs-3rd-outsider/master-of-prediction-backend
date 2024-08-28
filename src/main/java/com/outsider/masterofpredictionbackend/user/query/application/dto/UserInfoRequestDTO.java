package com.outsider.masterofpredictionbackend.user.query.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import lombok.Data;

@Data
public class UserInfoRequestDTO {

    private String email;
    private String userName;
    private Integer age;
    private Gender gender;
    private Location location;
    private Authority authority;
    private String token;

    public UserInfoRequestDTO(String email,  String userName, int age, Gender gender, Location location, Authority authority) {
        this.email = email;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.authority = authority;
    }
}
