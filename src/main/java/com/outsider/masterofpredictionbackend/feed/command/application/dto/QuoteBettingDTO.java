package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteBettingDTO {
    private Long quoteBettingId;
    private String quoteBettingTitle;
    private String quoteUserName;
    private String quoteDisplayName;
    private String quoteUserImg;
    private String quoteTierName;
    private String quoteBlindName;
    private List<String> imgUrls;
}
