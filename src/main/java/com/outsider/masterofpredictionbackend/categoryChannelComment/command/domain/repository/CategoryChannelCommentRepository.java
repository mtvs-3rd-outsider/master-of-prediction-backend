package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.repository;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.model.CategoryChannelComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryChannelCommentRepository extends JpaRepository<CategoryChannelComment, Long> {
}
