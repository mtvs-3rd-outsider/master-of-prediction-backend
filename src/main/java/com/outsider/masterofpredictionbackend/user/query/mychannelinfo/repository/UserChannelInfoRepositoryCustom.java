package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository;


import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;

import java.util.Optional;

public interface UserChannelInfoRepositoryCustom {
    Optional<MyChannelInfoQueryModel> findMyChannelInfoByUserId(Long userId);
}
