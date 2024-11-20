package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Tier;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UserDTO {
    private Long userId;
    private String userName;
    private String displayName;
    private BigDecimal points;
    private Authority authority;
    private Tier tier;
    private String userImg;

    public UserDTO() {
    }

    public UserDTO(Long userId, String userName, String displayName, Authority authority, String userImg) {
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.authority = authority;
        this.userImg = userImg;
    }
}
