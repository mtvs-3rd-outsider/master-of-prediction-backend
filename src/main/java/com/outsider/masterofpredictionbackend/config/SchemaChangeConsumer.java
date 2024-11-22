package com.outsider.masterofpredictionbackend.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SchemaChangeConsumer {

    private final ElasticsearchService elasticsearchService;
    private final MongoDBService mongoDBService;

    public SchemaChangeConsumer(ElasticsearchService elasticsearchService, MongoDBService mongoDBService) {
        this.elasticsearchService = elasticsearchService;
        this.mongoDBService = mongoDBService;
    }

    @KafkaListener(topics = "schemahistory.forceasthub", groupId = "schema-change-group")
    public void consumeSchemaChange(ConsumerRecord<String, String> record) {
        System.out.printf("Received DDL Event: %s%n", record.value());

        try {
            // DDL 이벤트 분석 및 처리
            processSchemaChange(record.value());
        } catch (Exception e) {
            System.err.printf("Failed to process schema change: %s%n", e.getMessage());
        }
    }

    private void processSchemaChange(String ddlEvent) {
        // DDL 이벤트 파싱 (JSON 형식)
        if (ddlEvent.contains("DROP TABLE")) {
            System.out.println("Detected DROP TABLE event. Resetting databases...");

            // Elasticsearch 및 MongoDB 동기화
            elasticsearchService.resetAllIndexes();
            mongoDBService.resetAllCollections();

            System.out.println("Databases reset successfully.");
        }
    }
}
