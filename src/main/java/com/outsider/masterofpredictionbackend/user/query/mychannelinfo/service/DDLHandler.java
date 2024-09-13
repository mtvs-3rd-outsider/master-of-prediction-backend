package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class DDLHandler {
    private final AdminClient adminClient;

    private final ObjectMapper mapper;
    private final MyChannelInfoRepository repository;
    public DDLHandler(AdminClient adminClient, ObjectMapper mapper, MyChannelInfoRepository repository) {
        this.adminClient = adminClient;
        this.mapper = mapper;
        this.repository = repository;
    }

    /**
     * DDL 이벤트를 처리하는 리스너
     */
//    @KafkaListener(topics = "schema-changes-forecasthub.user", groupId = "my-channel-info-ddl-group")
    @Transactional
    public void consumeSchemaChanges(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        String consumedValue = record.value();

        if (consumedValue == null) {
            log.error("Consumed schema change record value is null for record: {}", record);
            return;
        }

        try {
            JsonNode jsonNode = mapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            JsonNode source = payload.path("source");
            String ddlStatement = payload.get("ddl").asText();

            if (isDropTable(ddlStatement)) {
                // DDL 문에서 데이터베이스와 테이블 이름 추출
                String database = source.get("db").asText();
                String table = source.get("table").asText();

                log.info("Received DROP TABLE event for {}.{}", database, table);

                // 특정 테이블과 연관된 조회 모델을 삭제
                deleteReadModelForTable(database, table);
            }

            // 수동으로 오프셋 커밋
            consumer.commitSync();
        } catch (Exception e) {
            log.error("Unexpected error occurred while consuming schema change record: {}", record, e);
            // 재처리 로직
//            retryProcessing(record, consumer);
        }
    }

    /**
     * DDL 문이 DROP TABLE인지 확인하는 메서드
     */
    private boolean isDropTable(String ddlStatement) {
        return ddlStatement.toLowerCase().startsWith("drop table");
    }

    /**
     * 특정 테이블에 대한 조회 모델을 삭제하는 메서드
     */
    private void deleteReadModelForTable(String database, String table) {
        try {
            // MyChannelInfoRepository에 모든 문서를 삭제하는 메서드 사용
            // 특정 테이블과 연관된 필드가 없으므로 모든 문서를 삭제
            repository.deleteAll();
            log.info("Read model for table {}.{} has been deleted.", database, table);

            // Delete the associated Kafka topic
            String topicName = deriveTopicName(database, table);
            deleteKafkaTopic(topicName);
        } catch (Exception e) {
            log.error("Error deleting read model for table {}.{}: {}", database, table, e.getMessage(), e);
        }
    }
    /**
     * Kafka 토픽 이름을 결정하는 메서드
     * 필요에 따라 주제 이름 결정 로직을 수정하세요.
     */
    private String deriveTopicName(String database, String table) {
        // 예시: database.table 형태로 토픽 이름을 구성
        return "dbserver1."+ database + "." + table;
    }
    /**
     * Kafka 토픽을 삭제하는 메서드
     */
    private void deleteKafkaTopic(String topicName) {
        try {
            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topicName));
            deleteTopicsResult.all().get(); // 동기적으로 대기
            log.info("Kafka topic '{}' has been deleted successfully.", topicName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            log.error("Thread was interrupted while deleting Kafka topic '{}': {}", topicName, e.getMessage(), e);
        } catch (ExecutionException e) {
            log.error("Error deleting Kafka topic '{}': {}", topicName, e.getMessage(), e);
        }
    }

}
