package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@ToString
public class CategoryChannelUserCounts {

    @Column(name = "JOIN_COUNT")
    private int joinCount;

    public CategoryChannelUserCounts() {}

    public CategoryChannelUserCounts(int joinCount) {
        validJoinCount(joinCount);
        this.joinCount = joinCount;
    }

    private void validJoinCount(int joinCount) {
        if (joinCount < 0) {
            throw new IllegalArgumentException("Join count cannot be negative");
        }
    }

    public void incrementJoinCount() {
        joinCount++;
    }

    public void decrementJoinCount() {
        if (joinCount > 0) {
            joinCount--;
        }
    }
}
