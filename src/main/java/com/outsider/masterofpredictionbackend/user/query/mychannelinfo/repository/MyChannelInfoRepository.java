package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository;


import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyChannelInfoRepository extends MongoRepository<MyChannelInfoQueryModel, Long> {
    Optional<MyChannelInfoQueryModel> findById(Long id);
}
