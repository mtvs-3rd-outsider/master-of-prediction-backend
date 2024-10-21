package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.repository;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.Anonymous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnonymousCommentRepository extends JpaRepository<Anonymous, Long> {
    Anonymous findTopByChannelIdOrderByAnonymousNoDesc(Long channelId);
}
