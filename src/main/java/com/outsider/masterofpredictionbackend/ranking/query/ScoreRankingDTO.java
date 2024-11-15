package com.outsider.masterofpredictionbackend.ranking.query;

import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.ScoreRanking;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScoreRankingDTO {

    private Long userId;
    private int score;
    private int rank;
    private LocalDateTime lastUpdated;
    private String userName;
    private String displayName;
    private String userImg;

    public ScoreRankingDTO() {}
}
