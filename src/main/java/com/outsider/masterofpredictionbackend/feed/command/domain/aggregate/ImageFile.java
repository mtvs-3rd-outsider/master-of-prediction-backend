package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("IMAGE")
@NoArgsConstructor
public class ImageFile extends MediaFile {
    public ImageFile(String fileUrl, Feed feed) {
        super(fileUrl, feed);
    }
}