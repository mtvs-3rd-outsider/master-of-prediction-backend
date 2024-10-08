package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.repository;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryChannelCommentRepository extends JpaRepository<CategoryChannelComment, Long> {

    List<CategoryChannelComment> findAllByChannelId(Long channelId);
}
