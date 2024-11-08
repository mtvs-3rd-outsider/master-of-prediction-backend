package com.outsider.masterofpredictionbackend.dm.query;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class ReactionVM {
    private Integer reactionId;      // Unique ID for the reaction
    private String reactionType;     // Reaction emoji type
    private String userId;           // User ID who reacted
    private String userName;         // User name who reacted
    private String displayName;      // Display name who reacted
    private String userImg;
    public ReactionVM(Integer reactionId, String reactionType, String userId, String userName, String displayName,String userImg) {
        this.reactionId = reactionId;
        this.reactionType = reactionType;
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.userImg = userImg;
    }
}

