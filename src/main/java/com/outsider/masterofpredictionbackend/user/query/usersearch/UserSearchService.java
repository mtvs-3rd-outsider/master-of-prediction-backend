package com.outsider.masterofpredictionbackend.user.query.usersearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.util.GenericService;
import com.outsider.masterofpredictionbackend.util.IDs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSearchService extends GenericService<UserSearchModel,Long, User> {

    private static final Logger logger = LoggerFactory.getLogger(UserSearchService.class);
    private final ObjectMapper objectMapper;

    public UserSearchService(UserSearchRepository userSearchRepository, UserSearchRepository userSearchRepository1, ObjectMapper objectMapper) {
        super(userSearchRepository, User.class);
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.user", groupId = "user-search-group")
    @Transactional
    public void consume(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        String consumedValue = record.value();

        if (consumedValue == null) {
            logger.error("Consumed record value is null for record: {}", record);
            return;
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            String operation = payload.get("op").asText().substring(0, 1);
            JsonNode after = payload.path("after");
            JsonNode before = payload.path("before");

            switch (operation) {
                case "c":
                case "u":
                    handleCreateOrUpdate(after);
                    break;
                case "d":
                    handleDelete(before);
                    break;
                default:
                    logger.warn("Unknown operation type: {}", operation);
            }

            // 수동으로 오프셋 커밋
            consumer.commitSync();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while consuming record: {}", record, e);

            // 재처리 로직 - 메시지 처리 실패 시 재시도하거나 별도의 큐에 추가하는 방식으로 처리
            retryProcessing(record, consumer);
        }
    }

    public void handleCreateOrUpdate(JsonNode jsonNode) {
        //TODO: 여기에 ID 바꾸면됨
        Long userId = jsonNode.get(IDs.USER_ID).asLong();
        saveOrUpdate(jsonNode, userId, UserSearchModel.class);
    }

    public void handleDelete(JsonNode jsonNode) {
        //TODO: 여기에 ID 바꾸면됨
        Long userId = jsonNode.get(IDs.USER_ID).asLong();
        deleteById(userId);
    }

}
