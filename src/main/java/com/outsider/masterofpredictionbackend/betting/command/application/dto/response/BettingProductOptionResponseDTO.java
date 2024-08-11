package com.outsider.masterofpredictionbackend.betting.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BettingProductOptionResponseDTO {

    private Long id;

    private String content;

    private String imgUrl;

}
