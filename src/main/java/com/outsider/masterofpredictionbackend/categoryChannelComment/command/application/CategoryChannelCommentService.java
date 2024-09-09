package com.outsider.masterofpredictionbackend.categoryChannelComment.command.application;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentAddRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentDeleteRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.Content;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.repository.CategoryChannelCommentRepository;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.CategoryChannelCommentPolicy;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.dto.LoginUserInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.exception.CategoryChannelCommentNotFoundException;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.exception.CategoryChannelCommentPasswordMisMatchException;
import com.outsider.masterofpredictionbackend.common.exception.MisMatchUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryChannelCommentService {
    private final CategoryChannelCommentPolicy policy;
    private final CategoryChannelCommentRepository repository;

    public Long addComment(CategoryChannelCommentAddRequestDTO comment) {
        CategoryChannelComment saveComment;

        /*익명 사용자 댓글*/
        if(!policy.isLogin()){
            saveComment = new CategoryChannelComment(
                    policy.generateAnonymousUser(),
                    new Content(
                            comment.getComment(),
                            comment.getImageUrl(),
                            comment.getPassword()
                    )
            );
        }
        
        /*로그인 사용자 댓글 작성*/
        LoginUserInfo loginUserInfo = policy.getLoginUserInfo();

        saveComment = new CategoryChannelComment(
                new WriterInfo(
                        loginUserInfo.getUserNo(),
                        loginUserInfo.getUserName(),
                        true
                ),
                new Content(
                        comment.getComment(),
                        comment.getImageUrl(),
                        comment.getPassword()
                )
        );
        repository.save(saveComment);

        log.info("[CategoryChannelComment] 댓글 작성. 작성자 id: {}, 댓글 id: {}",
                saveComment.getWriter().getWriterNo(),
                saveComment.getId()
        );

        return saveComment.getId();
    }

    public boolean updateComment(CategoryChannelCommentUpdateRequestDTO updateComment) {

        policy.isUpdatable(updateComment);

        CategoryChannelComment comment = policy.getCommentById(updateComment.getCommentId())
                .orElseThrow( () ->
                new CategoryChannelCommentNotFoundException(
                        "[CategoryChannelComment] 수정할 베팅 댓글을 못찾음. " +
                                "id: " + updateComment.getCommentId(),
                        "수정할 배팅 댓글을 찾지 못했습니다."
                )
        );

        /*시큐리티에서 검증함*/
        /*수정하려는 사람과 댓글 작성자가 일치하는지 여부 확인*/
        if(!policy.isMatchUserInfo(comment)){
            throw new MisMatchUserException(
                    "[CategoryChannelComment] 작성자와 수정할 사람이 다름: " +
                            "작성자: " + comment.getWriter().getWriterNo() +
                            " 수정할 사람: " + policy.getLoginUserInfo().getUserNo(),
                    "댓글 작성자와 정보가 일치하지 않습니다."
            );
        }

        /*업데이트 할 댓글 객체 수정*/
        comment.setContent(
                new Content(
                        updateComment.getUpdatedComment(),
                        updateComment.getUpdatedImageUrl(),
                        updateComment.getPassword()
                )
        );

        /*댓글 내용 업데이트*/
        repository.save(comment);

        log.info("[BettingComment] 베팅 댓글 업데이트됨. id: {}, 업데이트 내용: {}",
                comment.getId(),
                comment.getContent()
        );

        return true;
    }

    void deleteComment(CategoryChannelCommentDeleteRequestDTO deleteRequestDTO) {

        /*삭제할 댓글 객체 가져옴*/
        CategoryChannelComment deleted = policy.getCommentById(deleteRequestDTO.getCommentId())
                .orElseThrow(() ->
                new CategoryChannelCommentNotFoundException(
                        "[CategoryChannelComment] 삭제할 댓글을 못찾음. " +
                                "댓글 id: " + deleteRequestDTO.getCommentId(),
                        "삭제할 댓글을 찾지 못했습니다.")
                );

        /*로그인시*/
        if(policy.isLogin()){
            /*삭제하려는 사용자와 로그인 한 사용자가 일치하는지 검증*/
            if(policy.isMatchUserInfo(deleted)){

                /*soft delete 실행*/
                deleted.setDeletedAt(LocalDateTime.now());
                repository.save(deleted);
            } else{
                /*로그인한 사용자와 댓글 작성자가 일치하지 않을때*/
                throw new MisMatchUserException(
                        "[CategoryChannelComment] 삭제할 사용자가 일치하지 않음. " +
                                "댓글 id: "+ deleted.getId()+
                                "작성자: " + deleted.getWriter().getWriterNo() +
                                "접근한 사람: " + policy.getLoginUserInfo().getUserNo(),
                        "댓글 작성자와 사용자 정보가 일치하지 않습니다."
                        );
            }
        } else{ /*비 로그인시*/

            /*비밀번호 일치시*/
            if(deleteRequestDTO.getPassword().equals(deleted.getContent().getPassword()) ){
                deleted.setDeletedAt(LocalDateTime.now());
                repository.save(deleted);
            } else{
                /*비밀번호 틀렸을때*/
                throw new CategoryChannelCommentPasswordMisMatchException(
                        "[CategoryChannelComment] 비밀번호가 일치하지 않음. " +
                                "댓글 id: " + deleted.getId() +
                                "비밀번호: " + deleted.getContent().getPassword() +
                                "입력 비밀번호: " + deleteRequestDTO.getPassword(),
                        "댓글 비밀번호와 일치하지 않습니다."
                );
            }
        }

    }
}
