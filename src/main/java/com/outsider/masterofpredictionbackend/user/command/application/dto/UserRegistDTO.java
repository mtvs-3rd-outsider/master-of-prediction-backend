package com.outsider.masterofpredictionbackend.user.command.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.ProviderInfo;
import lombok.Data;

@Data
public class UserRegistDTO {

    private String email;
    private String password;
    private String userName;
    private Authority authority;
    private ProviderInfo providerInfo;  // 추가된 ProviderInfo 필드

    // 기본적인 필드 (권한 포함)
    public UserRegistDTO(String email, String password, String userName, Authority authority) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.authority = authority;
        this.providerInfo = new ProviderInfo();
    }

    // SignUpRequestDTO와 권한을 받아 처리하는 생성자
    public UserRegistDTO(SignUpRequestDTO signUpRequestDTO, Authority authority) {
        this.email = signUpRequestDTO.getEmail();
        this.password = signUpRequestDTO.getPassword();
        this.userName = signUpRequestDTO.getUserName();
        this.authority = authority;
        this.providerInfo = new ProviderInfo();
    }

    // 추가된 필드 (gender, location, providerInfo) 포함하는 생성자
    public UserRegistDTO(String email, String password, String userName, Authority authority,  ProviderInfo providerInfo) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.authority = authority;
        this.providerInfo = providerInfo;  // ProviderInfo 필드 추가
    }

    // 기본 생성자 (필수)
    public UserRegistDTO() {
    }
}
