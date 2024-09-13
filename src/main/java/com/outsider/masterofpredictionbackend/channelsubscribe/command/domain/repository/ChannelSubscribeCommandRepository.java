package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.repository;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelSubscribeCommandRepository extends JpaRepository<ChannelSubscribe, MyChannelSubscribeId>
{
    Optional<ChannelSubscribe> findByIdAndIsActive(MyChannelSubscribeId id, Boolean isActive);
}