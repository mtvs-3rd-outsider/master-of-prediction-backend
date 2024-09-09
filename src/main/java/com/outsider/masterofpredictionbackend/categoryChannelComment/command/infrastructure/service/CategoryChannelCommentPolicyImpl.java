package com.outsider.masterofpredictionbackend.categoryChannelComment.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.repository.CategoryChannelCommentRepository;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.CategoryChannelCommentPolicy;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.dto.LoginUserInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.exception.CategoryChannelCommentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/* TODO: 타 도메인과 연결*/
@Service
public class CategoryChannelCommentPolicyImpl implements CategoryChannelCommentPolicy {
    private final CategoryChannelCommentRepository commentRepository;

    public CategoryChannelCommentPolicyImpl(CategoryChannelCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

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
    public boolean isMatchUserInfo(CategoryChannelComment comment) {

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

    @Override
    public boolean isMatchPassword(CategoryChannelComment comment, String password) {

        String commentPassword = comment.getContent().getPassword();

        if(commentPassword == null){// 로그인시
            return true;
        } else{
            return commentPassword.equals(password);
        }
    }

    @Override
    public WriterInfo generateAnonymousUser(){
        return new WriterInfo(
                null,
                "익명 유저",
                false
        );
    }

    @Override
    public boolean isUpdatable(CategoryChannelCommentUpdateRequestDTO updateRequestDTO){

        CategoryChannelComment comment = commentRepository.findById(updateRequestDTO.getCommentId()).orElseThrow( () ->
                new CategoryChannelCommentNotFoundException(
                        "[CategoryChannelComment] 댓글을 찾을 수 없음. " +
                                "id: " + updateRequestDTO.getCommentId(),
                        "수정할 댓글이 존재하지 않습니다.")
        );

        /*로그인 하지 않은 경우*/
        if(!isLogin()){
            if(comment.getContent().getPassword().equals(updateRequestDTO.getPassword())){// 비밀번호가 일치할때
                return true;
            }

            /*비밀번호가 일치하지 않을때*/
            return false;
        }

        /*로그인 한 경우*/

        /*관리자는 모든 댓글 수정 가능*/
        if(getLoginUserInfo().getIsAdmin()){
            return true;
        }

        /*로그인 한 사용자와 작성자가 일치하면 수정 가능.*/
        if(getLoginUserInfo().getUserNo().equals(comment.getWriter().getWriterNo()) ){
            return true;
        }

        /*그렇지 않으면 false 반환*/
        return false;
    }

    @Override
    public Optional<CategoryChannelComment> getCommentById(Long id){
        Optional<CategoryChannelComment> rawComment = commentRepository.findById(id);

        /*관리자는 soft delete 된것도 조회 할 수 있어야 함.*/
        if(isLogin()){
            if(getLoginUserInfo().getIsAdmin()){
                return rawComment;
            }
        }

        /*관리자가 아니면 값이 있을때만 전달함*/
        if(rawComment.isPresent()){
            if(rawComment.get().getDeletedAt() == null){
                return rawComment;
            }
        }

        /*값이 없을때는 empty를 전달함.*/
        return Optional.empty();

    }
}
