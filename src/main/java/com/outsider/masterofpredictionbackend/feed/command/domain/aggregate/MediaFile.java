package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "file_type")
@Table(name = "tbl_feed_media_file")
@Getter
@Setter
@NoArgsConstructor
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private long id;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    public MediaFile(String fileUrl, Feed feed) {
        this.fileUrl = fileUrl;
        this.feed = feed;
    }

    public MediaFile(String url) {
        this.fileUrl = url;
    }
}
