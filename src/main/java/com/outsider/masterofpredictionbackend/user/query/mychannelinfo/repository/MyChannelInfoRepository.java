package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository;


import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoViewModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyChannelInfoRepository extends MongoRepository<MyChannelInfoViewModel, Long> {
    Optional<MyChannelInfoViewModel> findById(Long id);
}
