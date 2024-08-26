package com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_quotefeed_image_file")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotefeed_id", nullable = false)
    private QuoteFeed quoteFeed;

    public ImageFile(String imageUrl, QuoteFeed quoteFeed) {
        this.imageUrl = imageUrl;
        this.quoteFeed = quoteFeed;
    }

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", quoteFeed=" + quoteFeed +
                '}';
    }
}
