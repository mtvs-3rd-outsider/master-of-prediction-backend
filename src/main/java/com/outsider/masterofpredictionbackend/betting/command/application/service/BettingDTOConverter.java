package com.outsider.masterofpredictionbackend.betting.command.application.service;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.response.BettingProductAndOptionResponseDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.response.BettingProductOptionResponseDTO;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;

import java.util.ArrayList;
import java.util.List;

class BettingDTOConverter {

    public static BettingProductAndOptionResponseDTO bettingEntityConvertToDTO(BettingProduct bettingProduct, List<BettingProductImage> bettingProductImages, List<BettingProductOptionResponseDTO> bettingProductOptionResponseDTOS) {

        return (new BettingProductAndOptionResponseDTO(
                bettingProduct.getId(),
                bettingProduct.getTitle(),
                bettingProduct.getContent(),
                bettingProduct.getCategoryCode(),
                bettingProduct.getDeadlineDate(),
                bettingProduct.getDeadlineTime(),
                bettingProductImages.stream().map(BettingProductImage::getImgUrl).toList(),
                bettingProductOptionResponseDTOS
        ));
    }

    public static List<BettingProductOptionResponseDTO> optionEntityConvertToDTO(List<BettingProductOption> bettingProductOptions) {
        return  bettingProductOptions.stream().map(
                bettingProductOption -> new BettingProductOptionResponseDTO(
                        bettingProductOption.getId(),
                        bettingProductOption.getContent(),
                        bettingProductOption.getImgUrl()
                )
        ).toList();
    }

    public static BettingProduct convertToBettingProduct(BettingProductAndOptionDTO bettingProductAndOptionDTO) {
        return new BettingProduct(
                bettingProductAndOptionDTO.getTitle(),
                bettingProductAndOptionDTO.getContent(),
                bettingProductAndOptionDTO.getUserId(),
                bettingProductAndOptionDTO.getCategoryCode(),
                bettingProductAndOptionDTO.getDeadlineDate(),
                bettingProductAndOptionDTO.getDeadlineTime(),
                bettingProductAndOptionDTO.getIsBlind(),
                bettingProductAndOptionDTO.getBlindName()
        );
    }

    public static List<BettingProductImage> convertToBettingProductImage(Long bettingId, List<String> mainImgUrls){
        List<BettingProductImage> bettingProductImages = new ArrayList<>();
        for (String mainImgUrl : mainImgUrls) {
            BettingProductImage bettingProductImage = new BettingProductImage(bettingId, mainImgUrl);
            bettingProductImages.add(bettingProductImage);
        }
        return bettingProductImages;
    }

    public static List<BettingProductOption> convertToBettingProductOption(
            Long bettingId,
            List<BettingProductOptionDTO> bettingProductOptionDTOS,
            List<String> optionImgUrls
    ){
        List<BettingProductOption> bettingProductOptions = new ArrayList<>();
        int size = bettingProductOptionDTOS.size();
        for (int i = 0; i < size; i++) {
            BettingProductOption bettingProductOption = new BettingProductOption(
                    bettingId,
                    bettingProductOptionDTOS.get(i).getContent(),
                    optionImgUrls.get(i)
            );
            bettingProductOptions.add(bettingProductOption);

        }
        return bettingProductOptions;
    }
}
