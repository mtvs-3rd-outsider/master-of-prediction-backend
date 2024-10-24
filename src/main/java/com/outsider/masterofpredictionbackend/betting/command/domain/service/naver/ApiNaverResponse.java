package com.outsider.masterofpredictionbackend.betting.command.domain.service.naver;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiNaverResponse {
    private int code;
    private boolean success;
    private Result result;

    // Getters and setters

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        private List<GameNaver> games;
        private int gameTotalCount;

        // Getters and setters
    }
}
