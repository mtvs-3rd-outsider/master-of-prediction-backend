package com.outsider.masterofpredictionbackend.bettingorder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO {

    private String userName;

    private String displayName;

    private String tierName;

    private String userImg;

    private BigDecimal point;

    private LocalDate orderDate;

    private LocalTime orderTime;

    private String content;
}
