package com.outsider.masterofpredictionbackend.betting.command.domain.service.naver;

import lombok.Data;

@Data
public class GameNaver {
    private String gameId;
    private String superCategoryId;
    private String categoryId;
    private String categoryName;
    private String gameDate;
    private String gameDateTime;
    private String stadium;
    private String homeTeamCode;
    private String homeTeamName;
    private int homeTeamScore;
    private String awayTeamCode;
    private String awayTeamName;
    private int awayTeamScore;
    private String winner;
    private String statusCode;
    private int statusNum;
    private String statusInfo;
    private boolean cancel;
    private boolean suspended;
    private boolean hasVideo;
    private String roundCode;
    private boolean reversedHomeAway;
    private String homeTeamEmblemUrl;
    private String awayTeamEmblemUrl;
    private boolean gameOnAir;
    private boolean widgetEnable;
    private String specialMatchInfo;
    private String seriesOutcome;
    private String roundTournamentInfo;
    private String matchRound;
    private int leg;
    private boolean hasPtSore;
    private int homePtScore;
    private int awayPtScore;
    private String aggregateWinner;
    private String phaseCode;
    private String groupName;
    private boolean neutralGround;
    private boolean postponed;
}
