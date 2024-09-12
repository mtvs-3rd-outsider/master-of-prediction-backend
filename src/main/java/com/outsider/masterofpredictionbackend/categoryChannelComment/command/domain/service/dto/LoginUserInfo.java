package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.dto;

import lombok.Data;

@Data
public class LoginUserInfo {
    private Long userNo;
    private String userName;
    private String nickName;
    private Boolean isAdmin;
    private String role;
}
