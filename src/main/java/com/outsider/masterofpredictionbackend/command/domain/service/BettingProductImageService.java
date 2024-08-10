package com.outsider.masterofpredictionbackend.command.domain.service;

import com.outsider.masterofpredictionbackend.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.command.domain.repository.BettingProductImageRepository;
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
        imageRepository.saveAll(images);
    }
}
