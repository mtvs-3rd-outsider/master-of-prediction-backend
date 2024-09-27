package com.outsider.masterofpredictionbackend.categorychannel.query.categorysearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryQueryService {

    private final CategorySearchRepository categorySearchRepository;

    public CategoryQueryService(CategorySearchRepository categorySearchRepository) {
        this.categorySearchRepository = categorySearchRepository;
    }

    // 페이징을 위한 메서드: displayName으로 검색
    public Page<CategorySearchModel> searchByDisplayName(String displayName, Pageable pageable) {
        return categorySearchRepository.findByDisplayName(displayName, pageable);
    }



    // 페이징을 위한 메서드: 모든 유저 검색
    public Page<CategorySearchModel> findAll(Pageable pageable) {
        return categorySearchRepository.findAll(pageable);
    }
}
