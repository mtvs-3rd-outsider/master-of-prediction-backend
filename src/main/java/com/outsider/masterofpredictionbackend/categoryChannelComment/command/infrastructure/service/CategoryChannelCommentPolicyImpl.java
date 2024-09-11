package com.outsider.masterofpredictionbackend.categoryChannelComment.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.repository.CategoryChannelCommentRepository;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.CategoryChannelCommentPolicy;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.dto.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryChannelCommentPolicyImpl implements CategoryChannelCommentPolicy {

    private final CategoryChannelCommentRepository commentRepository;


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
            return true;
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
    public Optional<CategoryChannelComment> getCommentById(Long id){
        Optional<CategoryChannelComment> rawComment = commentRepository.findById(id);

        /*관리자는 soft delete 된것도 조회 할 수 있어야 함.*/
        if(isLogin()){
            if(getLoginUserInfo().getIsAdmin()){
                return rawComment;
            }
        }

        /*관리자가 아니면 soft delete 되지 않은 경우만 댓글을 반환함*/
        if(rawComment.isPresent()){
            if(rawComment.get().getDeletedAt() == null){
                return rawComment;
            }
        }

        /*값이 없을때는 empty를 전달함.*/
        return Optional.empty();

    }
}
