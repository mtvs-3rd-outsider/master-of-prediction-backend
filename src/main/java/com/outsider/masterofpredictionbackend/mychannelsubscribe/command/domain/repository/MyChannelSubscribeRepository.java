package com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.repository;

import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.aggregate.MyChannelSubscribe;
import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyChannelSubscribeRepository extends JpaRepository<MyChannelSubscribe, MyChannelSubscribeId> {
}