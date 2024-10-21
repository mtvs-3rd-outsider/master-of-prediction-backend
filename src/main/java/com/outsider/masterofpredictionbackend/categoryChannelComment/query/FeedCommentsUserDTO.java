package com.outsider.masterofpredictionbackend.categoryChannelComment.query;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedCommentsUserDTO {

    private Long userID;

    private String userName;

    private String displayName;

    private String tierName;

    private String userImg;

    public FeedCommentsUserDTO(User user){
        this.userID = user.getId();
        this.userName = user.getUserName();
        this.displayName = user.getDisplayName();
        this.tierName = user.getTier().getName();
        this.userImg = user.getUserImg();
    }
}
