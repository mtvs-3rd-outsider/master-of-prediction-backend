package com.outsider.masterofpredictionbackend.peed.command.application.dto;

import com.outsider.masterofpredictionbackend.peed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.peed.command.domain.aggregate.embedded.User;

public class PeedDTO {
    private final String authorType;
    private final User user; // User 임베디드 객체
    private final Guest guest;
    private final String title;
    private final String content;
    private final String channelType;

    // Getters and constructors
    public PeedDTO(String authorType, User user, Guest guest, String title, String content, String channelType) {
        this.authorType = authorType;
        this.user = user;
        this.guest = guest;
        this.title = title;
        this.content = content;
        this.channelType = channelType;
    }

    public String getAuthorType() {
        return authorType;
    }

    public User getUser() {
        return user;
    }

    public Guest getGuest() {
        return guest;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getChannelType() {
        return channelType;
    }


}
