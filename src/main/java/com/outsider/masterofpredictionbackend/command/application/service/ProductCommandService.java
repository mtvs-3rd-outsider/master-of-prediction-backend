package com.outsider.masterofpredictionbackend.command.application.service;

import com.outsider.masterofpredictionbackend.command.application.dto.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.command.application.dto.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.command.domain.service.BettingProductService;
import com.outsider.masterofpredictionbackend.utils.ImageStorageService;
import com.outsider.masterofpredictionbackend.utils.InvalidImageException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCommandService {

    private final BettingProductService bettingProductService;

    @Autowired
    public ProductCommandService(BettingProductService bettingProductService) {
        this.bettingProductService = bettingProductService;
    }

    private BettingProduct dtoConvertTOBettingProduct(BettingProductAndOptionDTO bettingProductAndOptionDTO) {
        return new BettingProduct(
                bettingProductAndOptionDTO.getTitle(),
                bettingProductAndOptionDTO.getContent(),
                bettingProductAndOptionDTO.getCategoryCode(),
                bettingProductAndOptionDTO.getDeadlineDate(),
                bettingProductAndOptionDTO.getDeadlineTime()
        );
    }

    private List<BettingProductImage> dtoMainImgUrlsConvertBettingProductImage(Long bettingId, List<String> mainImgUrls){
        List<BettingProductImage> bettingProductImages = new ArrayList<>();
        for (String mainImgUrl : mainImgUrls) {
            BettingProductImage bettingProductImage = new BettingProductImage(bettingId, mainImgUrl);
            bettingProductImages.add(bettingProductImage);
        }
        return bettingProductImages;
    }

    private List<BettingProductOption> dtoOptionConvertBettingProductOption(
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


    private List<String> saveAndReturnImageNames(List<MultipartFile> files){
        return ImageStorageService.saveAndReturnImageNames(files);
    }

    @Transactional
    public void save(BettingProductAndOptionDTO bettingProductAndOptionDTO) {
        List<String> mainImgUrls = new ArrayList<>();
        List<String> optionImgUrls = new ArrayList<>();
        // java 16 부터 지원
        List<MultipartFile> tmpOptionImgUrls = bettingProductAndOptionDTO.getOption().stream().map(BettingProductOptionDTO::getImgUrl).toList();
        try{
            mainImgUrls = saveAndReturnImageNames(bettingProductAndOptionDTO.getMainImgUrl());
            optionImgUrls = saveAndReturnImageNames(tmpOptionImgUrls);
        }catch (InvalidImageException e){
            mainImgUrls.forEach(ImageStorageService::deleteImage);
            throw new InvalidImageException("image upload fail");
        }



        BettingProduct bettingProduct = dtoConvertTOBettingProduct(bettingProductAndOptionDTO);
        bettingProductService.save(bettingProduct);
        List<BettingProductImage> bettingProductImage = dtoMainImgUrlsConvertBettingProductImage(bettingProduct.getId(), mainImgUrls);
        List<BettingProductOption> bettingProductOptions = dtoOptionConvertBettingProductOption(bettingProduct.getId(), bettingProductAndOptionDTO.getOption(), optionImgUrls);

    //     id 저장 후 반환



    }

}
