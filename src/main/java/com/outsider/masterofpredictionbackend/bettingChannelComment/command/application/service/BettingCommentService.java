package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.service;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.AddCommentRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.Content;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.repository.BettingChannelCommentRepository;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service.BettingChannelCommentPolicy;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BettingCommentService {

    private final BettingChannelCommentRepository repository;
    private final BettingChannelCommentPolicy policy;

    public Boolean addComment(@Valid AddCommentRequestDTO dto){

        /*로그인 여부 검증*/
        if(!policy.isLogin()){
            return false;
        }

        /*사용자 정보 추출*/
        long userNo = policy.getLoginUserInfo().getUserNo();

        /*배팅 댓글 객체 생성*/
        BettingChannelComment comment = new BettingChannelComment(
                new Content(
                        dto.getComment(),
                        dto.getImageUrl()
                ),
                new WriterInfo(
                        userNo,
                        dto.getUsername()
                )
        );

        BettingChannelComment result = repository.save(comment);

        /*TODO: 커스텀 예외 반환*/
        if(result == null){
            return false;
        }

        log.info("댓글 추가: id: {}, 작성자: {}",result.getNo(), result.getWriter().getWriterNo() );

        return true;
    }



}
