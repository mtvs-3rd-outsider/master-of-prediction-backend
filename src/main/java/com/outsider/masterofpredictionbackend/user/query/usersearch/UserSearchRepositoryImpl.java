package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class UserSearchRepositoryImpl implements UserSearchRepositoryCustom {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;






    @Override
    public Page<UserSearchModel> findByDisplayNameOrContaining(String displayName, Pageable pageable) {
        // 공백을 제거한 문자열을 사용해 부분 일치 검색 구성
        String sanitizedDisplayName = displayName.replaceAll("\\s+", "");

        // Criteria 빌더 생성
        Criteria criteria = new Criteria("displayName").is(displayName) // 정확히 일치하는 경우
                .or(new Criteria("displayName").contains(sanitizedDisplayName)); // 공백 제거 후 포함된 경우

        // CriteriaQuery 생성
        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);

        // 검색 실행
        SearchHits<UserSearchModel> searchHits = elasticsearchOperations.search(query, UserSearchModel.class);

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
