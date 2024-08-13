package com.outsider.masterofpredictionbackend.betting.command.application.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BettingProductAndOptionResponseDTO {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private Long categoryCode;

    private LocalDate deadlineDate;

    private LocalTime deadlineTime;


    private List<String> mainImgUrl;

    private List<BettingProductOptionResponseDTO> option;

    protected BettingProductAndOptionResponseDTO() {
    }

    public BettingProductAndOptionResponseDTO(Long id, String title, String content, Long categoryCode, LocalDate deadlineDate, LocalTime deadlineTime, List<String> mainImgUrl, List<BettingProductOptionResponseDTO> option) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryCode = categoryCode;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
        this.mainImgUrl = mainImgUrl;
        this.option = option;
    }
}
