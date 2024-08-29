package com.outsider.masterofpredictionbackend.bettingChannelComment.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
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
        dummy.setRole("dummy role");
        dummy.setIsAdmin(false);

        return dummy;
    }

    @Override
    public boolean isMatchUserInfo(BettingChannelComment comment) {

        LoginUserInfo loginedUser = getLoginUserInfo();

        Long writerNo = comment.getWriter().getWriterNo();

        /*로그인 여부 확인*/
        if(!isLogin()) {
            return false;
        }

        /*작성자 정보 존재 여부 확인*/
        if(writerNo == null){
            return false;
        }

        /*작성자 일치 여부 확인*/
        if(comment.getWriter().getWriterNo().equals(loginedUser.getUserNo())) {
            return true;
        }

        /*관리자 여부*/
        if(loginedUser.getIsAdmin()){
            return false;
        }

        return false;
    }
}
