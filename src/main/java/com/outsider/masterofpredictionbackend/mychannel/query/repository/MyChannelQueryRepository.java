package com.outsider.masterofpredictionbackend.mychannel.query.repository;

import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyChannelQueryRepository extends JpaRepository<MyChannel, Long> {

}