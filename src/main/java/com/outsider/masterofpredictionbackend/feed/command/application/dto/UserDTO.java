package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Tier;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UserDTO {
    private Long id;
    private String userName;
    private String displayName;
    private BigDecimal points;
    private Authority authority;
    private Tier tier;
    private String userImg;
}
