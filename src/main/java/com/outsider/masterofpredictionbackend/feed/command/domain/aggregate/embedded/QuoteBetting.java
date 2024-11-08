package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteBetting {
    @Column(name = "quote_betting_id")
    private Long quoteBettingId;

    @Column(name = "quote_betting_title")
    private String quoteBettingTitle;

    @Column(name = "quote_betting_user_name")
    private String quoteUserName;

    @Column(name = "quote_betting_display_name")
    private String quoteDisplayName;

    @Column(name = "quote_betting_user_img")
    private String quoteUserImg;

    @Column(name = "quote_betting_tier_name")
    private String quoteTierName;

    @Column(name = "quote_betting_blind_name")
    private String quoteBlindName;

    @ElementCollection
    @CollectionTable(
            name = "quote_betting_img_urls",
            joinColumns = @JoinColumn(name = "feed_id")
    )
    @Column(name = "img_url")
    private List<String> imgUrls = new ArrayList<>();
}