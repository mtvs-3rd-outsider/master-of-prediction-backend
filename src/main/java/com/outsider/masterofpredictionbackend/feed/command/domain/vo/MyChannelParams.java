package com.outsider.masterofpredictionbackend.feed.command.domain.vo;

public class MyChannelParams {
    private final Long myChannelId;


    public MyChannelParams(Long myChannelId) {
        this.myChannelId = myChannelId;
    }

    public Long getMyChannelId(){
        return myChannelId;
    }
}
