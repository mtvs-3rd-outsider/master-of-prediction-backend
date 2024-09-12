package com.outsider.masterofpredictionbackend.feed.command.domain.repository;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedImageFileRepository extends JpaRepository<ImageFile, Long> {
}
