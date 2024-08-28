package com.outsider.masterofpredictionbackend.feed.command.domain.repository;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeVideoRepository extends JpaRepository<YouTubeVideo, Long> {
}
