package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service.dto.LoginUserInfo;

public interface BettingChannelCommentPolicy {

    boolean isLogin();

    LoginUserInfo getLoginUserInfo();

    boolean isMatchUserInfo(BettingChannelComment comment);
}
