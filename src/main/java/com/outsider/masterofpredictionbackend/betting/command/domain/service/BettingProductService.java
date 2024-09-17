package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductImageRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductOptionRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class BettingProductService {

    private final BettingProductRepository bettingRepository;
    private final BettingProductImageRepository bettingProductImageRepository;
    private final BettingProductOptionRepository bettingProductOptionRepository;



    @Autowired
    public BettingProductService(BettingProductRepository bettingRepository, BettingProductImageRepository bettingProductImageRepository, BettingProductOptionRepository bettingProductOptionRepository) {
        this.bettingRepository = bettingRepository;
        this.bettingProductImageRepository = bettingProductImageRepository;
        this.bettingProductOptionRepository = bettingProductOptionRepository;
    }

    public void save(
            List<BettingProductImage> bettingProductImages,
            List<BettingProductOption> bettingProductOptions
    ) {

        if (!bettingProductImages.isEmpty()){
            bettingProductImageRepository.saveAll(bettingProductImages);
        }
        if (bettingProductOptions.isEmpty() || bettingProductOptions.size() < 2){
            throw new IllegalArgumentException("betting option should be more than 2");
        }
        bettingProductOptionRepository.saveAll(bettingProductOptions);
    }

    public BettingProduct findById(Long id) {
        return bettingRepository.findById(id).orElse(null);
    }

    public boolean validateProductExistenceAndStatus(Long productId) {
        BettingProduct bettingProduct = findById(productId);
        if (bettingProduct == null) {
            return false;
        }
        if(LocalDate.now().isAfter(bettingProduct.getDeadlineDate())){
            return false;
        }
        return !LocalDate.now().isEqual(bettingProduct.getDeadlineDate()) ||
                !LocalTime.now().isAfter(bettingProduct.getDeadlineTime());
    }
}
