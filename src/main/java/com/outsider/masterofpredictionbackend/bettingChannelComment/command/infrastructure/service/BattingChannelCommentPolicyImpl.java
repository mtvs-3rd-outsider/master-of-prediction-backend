package com.outsider.masterofpredictionbackend.bettingChannelComment.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service.BettingChannelCommentPolicy;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service.dto.LoginUserInfo;
import org.springframework.stereotype.Service;

/*TODO: 타 도메인 서비스와 연결하기*/
@Service
public class BattingChannelCommentPolicyImpl implements BettingChannelCommentPolicy {
    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public LoginUserInfo getLoginUserInfo() {
        LoginUserInfo dummy = new LoginUserInfo();

        dummy.setUserNo(1L);
        dummy.setUserName("dummy userName");
        dummy.setNickName("dummy nickName");

        return dummy;
    }
}
