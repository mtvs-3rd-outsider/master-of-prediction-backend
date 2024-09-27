package com.outsider.masterofpredictionbackend.categorychannel.query.categorysearch;

import com.outsider.masterofpredictionbackend.user.query.usersearch.UserSearchModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search/category")
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    public CategoryQueryController(CategoryQueryService categoryQueryService) {
        this.categoryQueryService = categoryQueryService;
    }

    // displayName 검색에 페이징과 기본값 적용
    @GetMapping("/displayName")
    public Page<CategorySearchModel> searchByDisplayName(
            @RequestParam String q,
            @PageableDefault(page = 0, size = 10,  direction = Sort.Direction.ASC) Pageable pageable) {return categoryQueryService.searchByDisplayName(q, pageable);
    }
}
