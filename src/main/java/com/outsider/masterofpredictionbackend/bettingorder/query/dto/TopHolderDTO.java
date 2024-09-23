package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopHolderDTO {

    private String userName;

    private String displayName;

    private String tierName;

    private String userImg;

    private BigDecimal point;

    private Long bettingOptionId;
}
