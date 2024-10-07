package com.outsider.masterofpredictionbackend.betting.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingContentDTO {

    private LocalDate deadlineDate;
    private LocalTime deadlineTime;
    private Boolean isBlind;
    private String title;
    private String content;
    private Long userId;
    private String blindName;
}
