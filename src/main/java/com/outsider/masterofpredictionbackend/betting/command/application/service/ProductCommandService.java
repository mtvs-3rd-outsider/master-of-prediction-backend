package com.outsider.masterofpredictionbackend.betting.command.application.service;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.*;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.utils.ImageRollbackHelper;
import com.outsider.masterofpredictionbackend.utils.ImageStorageService;
import com.outsider.masterofpredictionbackend.utils.InvalidImageException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
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
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public ProductCommandService(BettingProductService bettingProductService, BettingProductImageService bettingProductImageService, BettingProductOptionService bettingProductOptionService, ImageRollbackHelper imageRollbackHelper, UserService userService, CategoryService categoryService) {
        this.bettingProductService = bettingProductService;
        this.bettingProductImageService = bettingProductImageService;
        this.bettingProductOptionService = bettingProductOptionService;
        this.imageRollbackHelper = imageRollbackHelper;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Transactional
    public Long save(BettingProductAndOptionDTO bettingProductAndOptionDTO) throws BadRequestException {
        if (!categoryService.isExistCategory(bettingProductAndOptionDTO.getCategoryCode()) ||
            !userService.isExistUser(bettingProductAndOptionDTO.getUserId())) {
            throw new BadRequestException("category or user not exist");
        }

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