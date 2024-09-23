package com.outsider.masterofpredictionbackend.bettingChannelComment.command.query;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BettingQueryCommentsRepository extends JpaRepository<BettingChannelComment, Long> {

    @Query("select new com.outsider.masterofpredictionbackend.bettingChannelComment.command.query.BettingQueryCommentsDTO(" +
            "bcc, " +
            "new com.outsider.masterofpredictionbackend.bettingChannelComment.command.query.BettingCommentsUserDTO (u))" +
            "from BettingChannelComment bcc " +
            "join User u on bcc.writer.writerNo = u.id " +
            "where bcc.bettingChannelId = :bettingChannelId order by bcc.createdAt desc")
    List<BettingQueryCommentsDTO> findByBettingChannelId(Long bettingChannelId);
}
