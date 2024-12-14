package com.outsider.masterofpredictionbackend.betting.query.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingUserDTO {
    private Long userID;

    private String userName;

    private String displayName;

    private String tierName;

    private String userImg;

    private Boolean isAdmin;

    public BettingUserDTO(Long userID, String userName, String displayName, String tierName, String userImg, Authority authority) {
        this.userID = userID;
        this.userName = userName;
        this.displayName = displayName;
        this.tierName = tierName;
        this.userImg = userImg;
        this.isAdmin = Authority.ROLE_ADMIN == authority;
    }
}
