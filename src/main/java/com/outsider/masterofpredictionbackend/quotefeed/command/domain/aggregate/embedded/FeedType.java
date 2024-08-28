package com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded;

import com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.FeedTypeEnum;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedType implements Serializable {

    private Long feeddTypeId;
    private FeedTypeEnum feedType; // "FEED" or "QUOTEFEED"

    @Override
    public String toString() {
        return "FeedType{" +
                "feeddTypeId=" + feeddTypeId +
                ", feedType='" + feedType + '\'' +
                '}';
    }
}