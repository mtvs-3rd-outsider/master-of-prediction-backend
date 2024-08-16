package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.repository;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BettingChannelCommentRepository extends JpaRepository<BettingChannelComment, Long> {
}
