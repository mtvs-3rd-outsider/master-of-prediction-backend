package com.outsider.masterofpredictionbackend.categoryChannelComment.query;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedQueryCommentsRepository extends JpaRepository<CategoryChannelComment, Long> {

    @Query("select new com.outsider.masterofpredictionbackend.categoryChannelComment.query.FeedQueryCommentsDTO(" +
            "ccc, " +
            "new com.outsider.masterofpredictionbackend.categoryChannelComment.query.FeedCommentsUserDTO(u))" +
            "from CategoryChannelComment ccc " +
            "join User u on ccc.writer.writerNo = u.id " +
            "where ccc.channelId = :channelId order by ccc.createdAt desc")
    List<FeedQueryCommentsDTO> findByChannelId(Long channelId);
}