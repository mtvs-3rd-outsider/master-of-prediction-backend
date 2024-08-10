package com.outsider.masterofpredictionbackend.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.ToString;

@Entity
@Table(name = "betting_product_option")
public class BettingProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long BettingId;

    @Column
    private String content;

    @Column
    private String imgUrl;

    protected BettingProductOption() {
    }

    public BettingProductOption(long bettingId, String content, String imgUrl) {
        BettingId = bettingId;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "BettingProductOption{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
