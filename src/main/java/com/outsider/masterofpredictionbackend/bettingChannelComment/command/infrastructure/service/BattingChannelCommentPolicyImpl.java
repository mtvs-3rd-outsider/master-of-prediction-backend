package com.outsider.masterofpredictionbackend.bettingChannelComment.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service.BettingChannelCommentPolicy;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import org.springframework.stereotype.Service;

/*TODO: 타 도메인 서비스와 연결하기*/
@Service
public class BattingChannelCommentPolicyImpl implements BettingChannelCommentPolicy {

    @Override
    public boolean isMatchUserInfo(BettingChannelComment comment, CustomUserInfoDTO loginUserInfo) {
        Long writerNo = comment.getWriter().getWriterNo();

        /*로그인 여부 확인*/
        if(loginUserInfo == null) {
            return false;
        }

        /*작성자 정보 존재 여부 확인*/
        if(writerNo == null){
            return false;
        }

        /*작성자 일치 여부 확인*/
        if(comment.getWriter().getWriterNo().equals(loginUserInfo.getUserId())) {
            return true;
        }

        /*관리자 여부*/
        if(loginUserInfo.getRole().getAuthority().equals("ROLE_ADMIN")){
            return false;
        }

        return false;
    }

    @Override
    public String generateAnonymousName() {
        return "익명댓글1";
    }
}
