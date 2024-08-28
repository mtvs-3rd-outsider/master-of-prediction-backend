package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "feed_image_file")
@Getter
@NoArgsConstructor
@Table(name = "tbl_feed_image_file")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    public ImageFile(String imageUrl, Feed feed) {
        this.imageUrl = imageUrl;
        this.feed = feed;
    }

    public ImageFile(String url) {
    }

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", feed=" + feed +
                '}';
    }

}
