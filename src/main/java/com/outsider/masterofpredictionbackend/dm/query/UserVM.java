package com.outsider.masterofpredictionbackend.dm.query;

import lombok.Data;

@Data
public class UserVM {

    private String name;
    private String avatarImageLink;
    private String id;
    private String userName;

    public UserVM(String name, String avatarImageLink, String id) {
        this.name = name;
        this.avatarImageLink = avatarImageLink;
        this.id = id;
    }
}
