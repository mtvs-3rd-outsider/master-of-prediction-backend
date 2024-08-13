package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BettingProductImageService {

    private final BettingProductImageRepository imageRepository;

    @Autowired
    public BettingProductImageService(BettingProductImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void save(BettingProductImage imageAggregate) {
        imageRepository.save(imageAggregate);
    }

    public void saveAll(List<BettingProductImage> images) {
        if (images.isEmpty()) {
            return;
        }
        imageRepository.saveAll(images);
    }
}
