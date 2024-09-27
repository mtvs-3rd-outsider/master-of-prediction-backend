package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.dto.LoginUserInfo;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;

import java.util.Optional;

public interface CategoryChannelCommentPolicy {

    LoginUserInfo getLoginUserInfo(CustomUserInfoDTO userInfoDTO);

    boolean isMatchUserInfo(CategoryChannelComment comment, CustomUserInfoDTO userInfoDTO);

    boolean isMatchPassword(CategoryChannelComment comment, String password);

    WriterInfo generateAnonymousUser();


    Optional<CategoryChannelComment> getCommentById(Long id, CustomUserInfoDTO userInfoDTO);
}
