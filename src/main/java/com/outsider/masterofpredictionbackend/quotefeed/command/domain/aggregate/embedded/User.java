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
public class User {

    @Column(name = "quotefeed_user_id")
    private String userId;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                '}';
    }

}