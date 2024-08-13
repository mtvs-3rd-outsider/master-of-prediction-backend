package com.outsider.masterofpredictionbackend.peed.command.domain.aggregate.embedded;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Guest {

    @Column(name = "post_guest_id")
    private String guestId;

    @Column(name = "post_guest_password")
    private String guestPassword;

    public Guest() {}

    public Guest(String guestId, String guestPassword) {
        this.guestId = guestId;
        this.guestPassword = guestPassword;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getGuestPassword() {
        return guestPassword;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId='" + guestId + '\'' +
                ", guestPassword='" + guestPassword + '\'' +
                '}';
    }
}