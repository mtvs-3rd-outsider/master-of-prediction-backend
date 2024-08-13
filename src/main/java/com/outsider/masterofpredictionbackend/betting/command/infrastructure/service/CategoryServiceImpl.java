package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public Boolean isExistCategory(Long categoryId) {
        if (categoryId == null || categoryId == 0){
            return false;
        }
        // TODO: 임의값
        return true;
    }
}
