package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
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

    public int getJoinCount() {
        return joinCount;
    }

    @Override
    public String toString() {
        return "CategoryChannelUserCounts{" +
                "joinCount=" + joinCount +
                '}';
    }
}
