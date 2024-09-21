package com.outsider.masterofpredictionbackend.betting.query.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BettingDetailDTO {

    private BettingUserDTO user;

    private BettingContentDTO product;

    private List<String> productImages;

    private List<BettingOptionDTO> options;

    public BettingDetailDTO(BettingUserDTO user, BettingContentDTO product, List<String> productImages, List<BettingOptionDTO> options) {
        this.user = user;
        this.product = product;
        this.productImages = productImages;
        this.options = options;
    }

    public BettingDetailDTO(BettingUserDTO user) {
        this.user = user;
    }
    public BettingDetailDTO(BettingUserDTO user, BettingContentDTO product){
        this.user = user;
        this.product = product;
    }
    public BettingDetailDTO(BettingUserDTO user, BettingContentDTO product, List<String> productImages){
        this.user = user;
        this.product = product;
        this.productImages = productImages;
    }
}
