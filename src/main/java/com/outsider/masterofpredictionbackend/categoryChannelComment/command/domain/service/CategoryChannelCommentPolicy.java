package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.dto.LoginUserInfo;

import java.util.Optional;

public interface CategoryChannelCommentPolicy {
    boolean isLogin();

    LoginUserInfo getLoginUserInfo();

    boolean isMatchUserInfo(CategoryChannelComment comment);

    boolean isMatchPassword(CategoryChannelComment comment, String password);

    WriterInfo generateAnonymousUser();

    boolean isUpdatable(CategoryChannelCommentUpdateRequestDTO updateRequestDTO);

    Optional<CategoryChannelComment> getCommentById(Long id);
}
