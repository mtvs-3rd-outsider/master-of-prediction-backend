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
    private String userName;
    private Integer age;
    private Gender gender;
    private Location location;
    private Authority authority;
    private  String token;
    private LocalDate birthday;
    private String avatarUrl;
    public UserInfoResponseDTO(Long id, String email, String userName, int age, Gender gender, Location location, Authority authority,LocalDate birthday, String avatarUrl ) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.authority = authority;
        this.birthday = birthday;
        this.avatarUrl= avatarUrl;
    }
}
