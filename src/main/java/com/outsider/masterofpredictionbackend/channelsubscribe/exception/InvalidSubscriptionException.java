package com.outsider.masterofpredictionbackend.channelsubscribe.exception;

public class InvalidSubscriptionException extends RuntimeException {

    public InvalidSubscriptionException() {
        super("Cannot subscribe to your own user channel.");
    }

    public InvalidSubscriptionException(String message) {
        super(message);
    }
}
