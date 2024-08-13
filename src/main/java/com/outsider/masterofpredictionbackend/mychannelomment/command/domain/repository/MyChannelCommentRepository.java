package com.outsider.masterofpredictionbackend.mychannelomment.command.domain.repository;

import com.outsider.masterofpredictionbackend.mychannelomment.command.domain.model.MyChannelComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyChannelCommentRepository extends JpaRepository<MyChannelComment, Long> {
}
