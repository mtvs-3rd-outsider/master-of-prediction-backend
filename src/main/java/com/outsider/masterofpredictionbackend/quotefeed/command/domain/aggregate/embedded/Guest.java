package com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate.embedded;


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

    @Column(name = "quotefeed_guest_id")
    private String guestId;

    @Column(name = "quotefeed_guest_password")
    private String guestPassword;

    @Override
    public String toString() {
        return "Guest{" +
                "guestId='" + guestId + '\'' +
                ", guestPassword='" + guestPassword + '\'' +
                '}';
    }

}