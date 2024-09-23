package com.outsider.masterofpredictionbackend.feed.command.domain.repository;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedImageFileRepository extends JpaRepository<MediaFile, Long> {
}
