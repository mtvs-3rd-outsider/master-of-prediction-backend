package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Guest {

    @Column(name = "feed_guest_id",nullable =true)
    private String guestId;

    @Column(name = "feed_guest_password",nullable =true)
    private String guestPassword;

    @Override
    public String toString() {
        return "Guest{" +
                "guestId='" + guestId + '\'' +
                ", guestPassword='" + guestPassword + '\'' +
                '}';
    }

}