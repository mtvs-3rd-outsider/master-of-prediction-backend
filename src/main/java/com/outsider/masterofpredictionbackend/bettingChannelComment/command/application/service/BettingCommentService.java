package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.service;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentAddRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.Content;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.repository.BettingChannelCommentRepository;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.service.BettingChannelCommentPolicy;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.exception.BettingCommentNotFoundException;
import com.outsider.masterofpredictionbackend.common.exception.LoginRequiredException;
import com.outsider.masterofpredictionbackend.common.exception.MisMatchUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BettingCommentService {

    private final BettingChannelCommentRepository repository;
    private final BettingChannelCommentPolicy policy;

    /**
     * 배팅 게시물에 댓글을 답니다.
     *
     * @param dto 입력 dto
     * @return 성공시 배팅댓글의 고유 id, 실패시 null 반환
     */
    public Long addComment(BettingCommentAddRequestDTO dto){

        /*시큐리티에서 검증함*/
        /*로그인 여부 검증*/
        if(!policy.isLogin()){
            throw new LoginRequiredException("[BettingComment] add: 로그인 필요", "로그인이 필요합니다.");
        }

        /*사용자 정보 추출*/
        long userNo = policy.getLoginUserInfo().getUserNo();

        /*배팅 댓글 객체 생성*/
        BettingChannelComment comment = new BettingChannelComment(
                dto.getBettingId(),
                new Content(
                        dto.getComment(),
                        dto.getImageUrl()
                ),
                new WriterInfo(
                        userNo
                )
        );

        BettingChannelComment result = repository.save(comment);

        if(result == null){
            return null;
        }

        log.info("댓글 추가: id: {}, 작성자: {}",result.getId(), result.getWriter().getWriterNo() );

        return result.getId();
    }

    /**
     * 배팅 게시물에 달린 댓글을 수정합니다
     *
     * @param updatedComment 입력 dto
     * @return 성공시 true, 실패시 false 반환
     */
    public Boolean updateComment(BettingCommentUpdateRequestDTO updatedComment) {
        
        /*시큐리티에서 검증함*/
        /*로그인 여부 확인*/
        if(!policy.isLogin()){
            throw new LoginRequiredException("[BettingComment] add: 로그인 필요", "로그인이 필요합니다.");
        }

        BettingChannelComment comment = repository.findById(updatedComment.getCommentId()).orElseThrow( () ->
                new BettingCommentNotFoundException(
                        "[BettingComment] 수정할 베팅 댓글을 못찾음. id: "+updatedComment.getCommentId(),
                        "수정할 배팅 댓글을 찾지 못했습니다."
                )
        );
        
        /*시큐리티에서 검증함*/
        /*수정하려는 사람과 댓글 작성자가 일치하는지 여부 확인*/
        if(!policy.isMatchUserInfo(comment)){
            throw new MisMatchUserException("[BettingComment] 작성자와 수정할 사람이 다름: 작성자: "+comment.getWriter().getWriterNo()+" 수정할 사람: "+policy.getLoginUserInfo().getUserNo(),
                    "댓글 작성자와 정보가 일치하지 않습니다."
            );
        }

        comment.setContent(
                new Content(
                        updatedComment.getUpdatedComment(),
                        updatedComment.getUpdatedImageUrl()
                )
        );

        /*댓글 내용 업데이트*/
        repository.save(comment);

        log.info("[BettingComment] 베팅 댓글 업데이트됨. id: {}, 업데이트 내용: {}", comment.getId(), comment.getContent() );
        return true;
    }

    /**
     * soft-delete 방식으로 배팅 게시물에 달린 댓글을 삭제합니다.
     *
     * @param commentId 댓글 id
     * @return 성공시 true, 실패시 false 반환
     */
    public Boolean deleteComment(Long commentId) {
        
        /*시큐리티에서 검증함*/
        /*로그인 여부 검증*/
        if(!policy.isLogin()){
            throw new LoginRequiredException("[BettingComment] delete: 로그인 필요", "로그인이 필요합니다.");
        }

        /*삭제할 데이터가 존재하는지 검증*/
        BettingChannelComment deletedComment = repository.findByIdAndDeletedAtIsNull(commentId).orElseThrow(() ->
                new BettingCommentNotFoundException("[BettingComment] 삭제할 댓글을 찾지 못함, id: "+commentId, "삭제할 댓글을 찾지 못했습니다.")
        );
        
        /*시큐리티에서 검증함*/
        /*삭제할 댓글이 본인이 작성한건지 확인*/
        if(!policy.isMatchUserInfo(deletedComment)){
            throw new MisMatchUserException("[BettingComment] 작성자와 삭제할 사람이 다름: 작성자: "+deletedComment.getWriter().getWriterNo()+" 삭제할 사람: "+policy.getLoginUserInfo().getUserNo(),
                    "댓글 작성자와 정보가 일치하지 않습니다."
            );
        }

        /*댓글 soft delete*/
        deletedComment.setDeletedAt(LocalDateTime.now());
        repository.save(deletedComment);

        log.info("[BettingComment] 댓글 삭제. id: {}", commentId);
        return true;
    }

}
