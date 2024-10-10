package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GuestDTO {
    private String guestId;

    private String guestPassword;

    public GuestDTO() {
    }

    public GuestDTO(String guestId, String guestPassword) {
        this.guestId = guestId;
        this.guestPassword = guestPassword;
    }
}
