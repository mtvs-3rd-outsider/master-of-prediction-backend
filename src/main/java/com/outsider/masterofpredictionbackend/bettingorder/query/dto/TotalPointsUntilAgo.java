package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TotalPointsUntilAgo {

    Long bettingOptionId;
    BigDecimal totalPoints;

}
