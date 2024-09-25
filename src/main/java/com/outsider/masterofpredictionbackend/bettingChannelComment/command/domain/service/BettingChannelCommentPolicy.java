package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;

public interface BettingChannelCommentPolicy {

    boolean isMatchUserInfo(BettingChannelComment comment, CustomUserInfoDTO loginUserInfo);
}
