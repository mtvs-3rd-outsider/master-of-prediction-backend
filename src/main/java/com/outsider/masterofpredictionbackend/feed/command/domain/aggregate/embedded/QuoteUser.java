package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class QuoteUser {

    @Column(name = "quote_user_id")
    private Long quoteUserId;

    public QuoteUser(Long quoteUserId) {
        this.quoteUserId = quoteUserId;
    }
}