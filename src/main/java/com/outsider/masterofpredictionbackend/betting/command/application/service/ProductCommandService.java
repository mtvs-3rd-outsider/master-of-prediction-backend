package com.outsider.masterofpredictionbackend.betting.command.application.service;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductState;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.*;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.file.MinioService;
import com.outsider.masterofpredictionbackend.utils.ImageRollbackHelper;
import com.outsider.masterofpredictionbackend.utils.InvalidImageException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductCommandService {

    private final BettingProductService bettingProductService;
    private final ImageRollbackHelper imageRollbackHelper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BettingProductRepository bettingProductRepository;
    private final MinioService minioService;

    public ProductCommandService(BettingProductService bettingProductService, ImageRollbackHelper imageRollbackHelper, UserService userService, CategoryService categoryService, BettingProductRepository bettingProductRepository, MinioService minioService) {
        this.bettingProductService = bettingProductService;
        this.imageRollbackHelper = imageRollbackHelper;
        this.userService = userService;
        this.categoryService = categoryService;
        this.bettingProductRepository = bettingProductRepository;
        this.minioService = minioService;
    }


    @Transactional
    public Long save(BettingProductAndOptionDTO bettingProductAndOptionDTO) throws BadRequestException {
        if (!categoryService.isExistCategory(bettingProductAndOptionDTO.getCategoryCode()) ||
            !userService.isExistUser(bettingProductAndOptionDTO.getUserId())) {
            throw new BadRequestException("category or user not exist");
        }

        List<String> mainImgUrls;
        List<String> optionImgUrls;
        List<MultipartFile> tmpOptionImgUrls = bettingProductAndOptionDTO.getOptions().stream().map(BettingProductOptionDTO::getImage).toList();

        try{
            mainImgUrls = saveAndReturnImageNames(bettingProductAndOptionDTO.getMainImgUrl());
            optionImgUrls = saveAndReturnImageNames(tmpOptionImgUrls);
        }catch (Exception e){
            log.error("betting create image upload fail: {}", e.getMessage());
            throw new InvalidImageException("image upload fail");
        }
        BettingProduct bettingProduct = BettingDTOConverter.convertToBettingProduct(bettingProductAndOptionDTO);
        bettingProduct.setState(BettingProductState.PROGRESS);
        BettingProduct saveBetting =  bettingProductRepository.save(bettingProduct);
        List<BettingProductImage> bettingProductImages = BettingDTOConverter.convertToBettingProductImage(saveBetting.getId(), mainImgUrls);
        List<BettingProductOption> bettingProductOptions = BettingDTOConverter.convertToBettingProductOption(saveBetting.getId(), bettingProductAndOptionDTO.getOptions(), optionImgUrls);
        try{
            bettingProductService.save(bettingProductImages, bettingProductOptions);
        }catch (IllegalArgumentException e){
            throw new BadRequestException(e.getMessage());
        }

        return bettingProduct.getId();
    }

    private List<String> saveAndReturnImageNames(List<MultipartFile> files) throws Exception {
        List<String> savedImageNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            savedImageNames.add(minioService.uploadFile(file));
        }
        return savedImageNames;
    }
}
