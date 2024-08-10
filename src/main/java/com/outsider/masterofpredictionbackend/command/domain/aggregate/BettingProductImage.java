package com.outsider.masterofpredictionbackend.command.domain.aggregate;

import jakarta.persistence.*;

@Entity
@Table(name = "betting_product_image")
public class BettingProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long bettingId;

    @Column
    private String imgUrl;

    protected BettingProductImage() {
    }

    public BettingProductImage(long bettingId, String imgUrl) {
        this.bettingId = bettingId;
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "BettingProductImage{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }


}
