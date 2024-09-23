package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("VIDEO")
@NoArgsConstructor
public class VideoFile extends MediaFile {
    public VideoFile(String fileUrl, Feed feed) {
        super(fileUrl, feed);
    }
}