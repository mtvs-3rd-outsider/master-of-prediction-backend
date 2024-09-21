package com.outsider.masterofpredictionbackend.betting.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingOptionDTO {

    private Long optionId;

    private String content;

    private String imgUrl;
}
