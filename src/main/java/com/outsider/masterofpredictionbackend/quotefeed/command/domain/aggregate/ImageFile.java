package com.outsider.masterofpredictionbackend.quotefeed.command.domain.aggregate;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name ="quotefeed_image_file")
@Getter
@NoArgsConstructor
@Table(name = "tbl_quotefeed_image_file")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotefeed_id", nullable = false)
    private QuoteFeed quoteFeed;

    public ImageFile(String fileUrl, QuoteFeed quoteFeed) {
        this.fileUrl = fileUrl;
        this.quoteFeed = quoteFeed;
    }

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", fileUrl='" + fileUrl + '\'' +
                ", quoteFeed=" + quoteFeed +
                '}';
    }
}
