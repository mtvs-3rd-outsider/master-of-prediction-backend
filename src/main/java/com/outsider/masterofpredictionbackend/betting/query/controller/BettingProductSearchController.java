package com.outsider.masterofpredictionbackend.betting.query.controller;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingSearchModel;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingSearchRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search/betting-product")
@Tag(name = "배팅 상품 검색 API", description = "배팅 상품 검색 API")
@Slf4j
public class BettingProductSearchController {

    private final BettingSearchRepository bettingSearchRepository;

    public BettingProductSearchController(BettingSearchRepository bettingSearchRepository) {
        this.bettingSearchRepository = bettingSearchRepository;
    }

    @GetMapping("/title")
    public Page<BettingSearchModel> searchByUserName(
            @RequestParam String q,
            @Parameter(description = "페이지 정보", schema = @Schema(type = "object", defaultValue = "{\n \"page\": 0,\n \"size\": 10\n}"))
            @PageableDefault(page = 0, size = 10,  direction = Sort.Direction.ASC) Pageable pageable) {
        return bettingSearchRepository.findByTitleOrContaining(q, pageable);
    }
}
