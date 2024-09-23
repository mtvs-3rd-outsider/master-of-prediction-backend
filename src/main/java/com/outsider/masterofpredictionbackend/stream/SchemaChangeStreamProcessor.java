package com.outsider.masterofpredictionbackend.stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.AlterConfigsOptions;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
//@Component
public class SchemaChangeStreamProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SchemaChangeStreamProcessor.class);
    private static final String INPUT_TOPIC = "dbserver1";
    private static final String SCHEMA_CHANGES_TOPIC_PREFIX = "schema-changes-";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AdminClient adminClient;

    public SchemaChangeStreamProcessor(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {

        // dbserver1 토픽으로부터 입력 스트림 생성
        KStream<String, String> sourceStream = streamsBuilder.stream(INPUT_TOPIC);

        // DDL 이벤트만 필터링
        KStream<String, String> schemaChangeStream = sourceStream.filter(
                (key, value) -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        JsonNode payload = jsonNode.path("payload");
                        return payload.has("ddl");
                    } catch (Exception e) {
                        logger.error("필터링 중 JSON 파싱 오류: {}", e.getMessage());
                        return false;
                    }
                }
        );

        // DDL 문을 수정
        KStream<String, String> modifiedSchemaChangeStream = schemaChangeStream.mapValues(
                value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        JsonNode payload = jsonNode.get("payload");
                        String ddlStatement = payload.get("ddl").asText();

                        // DDL 문을 대문자로 변환 (예시)
                        String modifiedDdlStatement = modifyDdlStatement(ddlStatement);

                        // 수정된 DDL 문을 payload에 반영
                        ((ObjectNode) payload).put("ddl", modifiedDdlStatement);

                        // 수정된 JSON 객체를 문자열로 반환
                        return jsonNode.toString();
                    } catch (Exception e) {
                        logger.error("매핑 중 JSON 파싱 오류: {}", e.getMessage());
                        return value;
                    }
                }
        );

        // 동적 토픽으로 라우팅
        modifiedSchemaChangeStream.to(
                (key, value, recordContext) -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        JsonNode payload = jsonNode.get("payload");
                        JsonNode source = payload.get("source");

                        // 데이터베이스 및 테이블 이름 추출
                        String database = source.get("db").asText();
                        String table = source.get("table").asText();

                        // 대상 토픽 이름 생성
                        String targetTopic = SCHEMA_CHANGES_TOPIC_PREFIX + database + "." + table;

                        // 토픽을 compact 정책으로 설정
                        //TODO: topic 정책 변경하는 코드
//                        createOrUpdateTopicWithCompactPolicy(targetTopic);

                        return targetTopic;
                    } catch (Exception e) {
                        logger.error("동적 토픽 라우팅 중 오류: {}", e.getMessage());
                        // 기본 토픽으로 라우팅
                        return SCHEMA_CHANGES_TOPIC_PREFIX + "unknown";
                    }
                },
                Produced.with(Serdes.String(), Serdes.String())
        );

        return sourceStream;
    }

    /**
     * DDL 문을 수정하는 메서드
     * 예시: DDL 문을 대문자로 변환
     */
    private String modifyDdlStatement(String ddlStatement) {
        return ddlStatement.toUpperCase();
    }

    /**
     * Compact 정책으로 토픽을 생성하거나 업데이트하는 메서드
     */
    private void createOrUpdateTopicWithCompactPolicy(String topicName) {
        try {
            // 토픽이 이미 존재하는지 확인
            KafkaFuture<TopicDescription> topicDescriptionFuture = adminClient.describeTopics(Collections.singletonList(topicName))
                    .values().get(topicName);

            topicDescriptionFuture.get();  // 이미 존재하면 넘어감
        } catch (Exception e) {
            // 토픽이 없으면 새로 생성
            createTopicWithCompactPolicy(topicName);
        }

        // compact 정책을 적용하도록 업데이트
        updateTopicCompactPolicy(topicName);
    }

    /**
     * Compact 정책이 적용된 새 토픽을 생성
     */
    private void createTopicWithCompactPolicy(String topicName) {
        try {
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
            Map<String, String> configs = new HashMap<>();
            configs.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
            newTopic.configs(configs);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            logger.info("토픽 {}이(가) 생성되었습니다.", topicName);
        } catch (Exception e) {
            logger.error("토픽 생성 중 오류: {}", e.getMessage());
        }
    }

    /**
     * 기존 토픽에 compact 정책 적용
     */
    private void updateTopicCompactPolicy(String topicName) {
        try {
            ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            ConfigEntry cleanupPolicy = new ConfigEntry(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
            Map<ConfigResource, Collection<AlterConfigOp>> configs = new HashMap<>();
            configs.put(configResource, Collections.singletonList(new AlterConfigOp(cleanupPolicy, AlterConfigOp.OpType.SET)));
            adminClient.incrementalAlterConfigs(configs, new AlterConfigsOptions()).all().get();
            logger.info("토픽 {}에 compact 정책이 적용되었습니다.", topicName);
        } catch (Exception e) {
            logger.error("토픽 업데이트 중 오류: {}", e.getMessage());
        }
    }
}
