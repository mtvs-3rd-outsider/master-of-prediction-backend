package com.outsider.masterofpredictionbackend.betting.command.application.service;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingProductImageService;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingProductOptionService;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingProductService;
import com.outsider.masterofpredictionbackend.utils.ImageRollbackHelper;
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
    private final BettingProductImageService bettingProductImageService;
    private final BettingProductOptionService bettingProductOptionService;
    private final ImageRollbackHelper imageRollbackHelper;

    @Autowired
    public ProductCommandService(BettingProductService bettingProductService, BettingProductImageService bettingProductImageService, BettingProductOptionService bettingProductOptionService, ImageRollbackHelper imageRollbackHelper) {
        this.bettingProductService = bettingProductService;
        this.bettingProductImageService = bettingProductImageService;
        this.bettingProductOptionService = bettingProductOptionService;
        this.imageRollbackHelper = imageRollbackHelper;
    }

    @Transactional
    public Long save(BettingProductAndOptionDTO bettingProductAndOptionDTO) {
        List<String> mainImgUrls = new ArrayList<>();
        List<String> optionImgUrls;
            List<MultipartFile> tmpOptionImgUrls = bettingProductAndOptionDTO.getOption().stream().map(BettingProductOptionDTO::getImgUrl).toList();

        try{
            mainImgUrls = saveAndReturnImageNames(bettingProductAndOptionDTO.getMainImgUrl());
            optionImgUrls = saveAndReturnImageNames(tmpOptionImgUrls);
        }catch (InvalidImageException e){
            mainImgUrls.forEach(ImageStorageService::deleteImage);
            throw new InvalidImageException("image upload fail");
        }
        mainImgUrls.forEach(imageRollbackHelper::addImageToDelete);
        optionImgUrls.forEach(imageRollbackHelper::addImageToDelete);
        imageRollbackHelper.registerForRollback();


        BettingProduct bettingProduct = BettingDTOConverter.convertToBettingProduct(bettingProductAndOptionDTO);
        bettingProductService.save(bettingProduct);
        List<BettingProductImage> bettingProductImages = BettingDTOConverter.convertToBettingProductImage(bettingProduct.getId(), mainImgUrls);
        bettingProductImageService.saveAll(bettingProductImages);
        List<BettingProductOption> bettingProductOptions = BettingDTOConverter.convertToBettingProductOption(bettingProduct.getId(), bettingProductAndOptionDTO.getOption(), optionImgUrls);
        bettingProductOptionService.saveAll(bettingProductOptions);
        return bettingProduct.getId();
        // List<BettingProductOptionResponseDTO> bettingProductOptionResponseDTOS = optionEntityConvertToDTO(bettingProductOptions);
        // return bettingEntityConvertToDTO(bettingProduct, bettingProductImages, bettingProductOptionResponseDTOS);
    }



    private List<String> saveAndReturnImageNames(List<MultipartFile> files){
        return ImageStorageService.saveAndReturnImageNames(files);
    }
}
