package com.outsider.masterofpredictionbackend.mychannel.command.domain.repository;

import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyChannelCommandRepository extends JpaRepository<MyChannel, Long> {
}