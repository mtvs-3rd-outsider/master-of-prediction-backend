package com.outsider.masterofpredictionbackend.user.command.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.embeded.Location;
import lombok.Data;

@Data
public class UserInfoRequestDTO {

    private String email;
    private String password;
    private String nickName;
    private Integer age;
    private Gender gender;
    private Location location;

    public UserInfoRequestDTO(String email, String password, String nickName, int age, Gender gender, Location location) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }

}
