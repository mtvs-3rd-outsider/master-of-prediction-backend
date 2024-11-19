package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import co.elastic.clients.elasticsearch.core.search.FieldCollapse;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingSearchModel;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingSearchRepositoryCustom;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class BettingSearchRepositoryImpl implements BettingSearchRepositoryCustom {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    //NOTE: 상품 검색결과
    @Override
    public Page<BettingSearchModel> findByTitleOrContaining(String title, Pageable pageable) {
        // 공백을 제거한 문자열을 사용해 부분 일치 검색 구성
        String sanitizedDisplayName = title.replaceAll("\\s+", "");

        // Criteria 빌더 생성
        Criteria criteria = new Criteria("title").is(title) // 정확히 일치하는 경우
                .or(new Criteria("title").contains(sanitizedDisplayName)); // 공백 제거 후 포함된 경우

        // CriteriaQuery 생성
        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);

        // 검색 실행
        SearchHits<BettingSearchModel> searchHits = elasticsearchOperations.search(query, BettingSearchModel.class);

        // 결과 변환 및 반환
        return new PageImpl<>(
                searchHits.getSearchHits()
                        .stream()
                        .map(hit -> hit.getContent())
                        .collect(Collectors.toList()),
                pageable,
                searchHits.getTotalHits()
        );
    }

}
