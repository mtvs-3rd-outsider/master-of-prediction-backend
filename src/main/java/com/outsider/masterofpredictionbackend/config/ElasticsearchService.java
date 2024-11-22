package com.outsider.masterofpredictionbackend.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public ElasticsearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    /**
     * 모든 인덱스를 삭제합니다.
     */
    public void resetAllIndexes() {
        try {
            // 모든 인덱스 가져오기
            GetIndexRequest getIndexRequest = GetIndexRequest.of(b -> b.index("*")); // 모든 인덱스 선택
            GetIndexResponse getIndexResponse = elasticsearchClient.indices().get(getIndexRequest);

            if (getIndexResponse.result().isEmpty()) {
                System.out.println("No indexes found in Elasticsearch cluster.");
                return;
            }

            // 인덱스 삭제
            for (String indexName : getIndexResponse.result().keySet()) {
                elasticsearchClient.indices().delete(DeleteIndexRequest.of(b -> b.index(indexName)));
                System.out.println("Deleted index: " + indexName);
            }

            System.out.println("All indexes have been successfully reset.");
        } catch (Exception e) {
            System.err.println("Failed to reset indexes in Elasticsearch.");
            e.printStackTrace();
        }
    }
}
