package com.outsider.masterofpredictionbackend.user.command.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import lombok.Data;
@Data
public class UserRegistDTO {
    private String email;
    private String password;
    private String userName;
    private Authority authority;

    public UserRegistDTO(String email, String password, String userName, Authority authority) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.authority = authority;
    }
}
