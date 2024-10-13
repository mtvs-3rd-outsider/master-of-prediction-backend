package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype;

public enum FeedConstants {
    DEFAULT_PAGE_SIZE(10);

    private final int value;

    FeedConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
