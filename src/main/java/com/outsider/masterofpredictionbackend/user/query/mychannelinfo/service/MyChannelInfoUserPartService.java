package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoViewModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Optional;

import static com.outsider.masterofpredictionbackend.util.Formatter.formatJoinDate;

@Service
public class MyChannelInfoUserPartService {

    private static final Logger logger = LoggerFactory.getLogger(MyChannelInfoUserPartService.class);

    private final MyChannelInfoRepository repository;
    private final ObjectMapper mapper;

    public MyChannelInfoUserPartService(MyChannelInfoRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "dbserver1.forecasthub.user")
    @Transactional
    public void consumeUser(ConsumerRecord<String, String> record) {
        String consumedValue = record.value();

        if (consumedValue == null) {
            logger.error("Consumed record value is null for record: {}", record);
            return;  // 혹은 적절히 처리
        }

        try {
            var jsonNode = mapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            String op = payload.get("op").asText().substring(0, 1);
            JsonNode after = payload.path("after");
            JsonNode before = payload.path("before");

            switch (op) {
                case "c":
                    handleCreate(after);
                    break;
                case "u":
                    handleUpdate(after);
                    break;
                case "d":
                    handleDelete(before);
                    break;
                default:
                    logger.warn("Unknown operation type: {}", op);
                    break;
            }
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON: {}", consumedValue, e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while consuming record: {}", record, e);
        }
    }

    private void handleCreate(JsonNode after) {
        if (after == null) {
            logger.warn("After payload is null during create operation");
            return;
        }

        MyChannelInfoViewModel mongoUser = MyChannelInfoViewModel.builder()
                .id(after.get("user_id").asLong()) // "user_id" 필드를 사용하여 id 설정
                .name(after.get("user_name").asText()) // "user_name" 필드를 사용하여 이름 설정
                .userEmail(after.get("user_email").asText()) // "user_email"을 username에 설정
                .birthdate(after.has("birthday") ? formatJoinDate(after, "birthday") : null) // "birthday"를 사용하여 생년월일 설정
                .joinedDate(formatJoinDate(after, "join_date")) // "join_date"를 문자열로 변환하여 설정
                .gender(after.has("user_gender") ? after.get("user_gender").asText() : null) // "user_gender"를 사용하여 성별 설정
                .points(new BigDecimal(Base64.getDecoder().decode(after.get("user_point").asText())[0] & 0xFF)) // Base64 디코딩하여 정수 값으로 변환 후 points 설정
                .tier_level(after.get("tier_level").asInt()) // "tier_level"을 tier_level에 설정
                .transactions(0) // transactions 필드는 초기값으로 설정 (데이터가 없는 경우)
                .profitRate(null) // profitRate는 초기값으로 null 설정
                .positionValue(null) // positionValue는 초기값으로 null 설정
                .tradeCount(0) // tradeCount는 초기값으로 설정
                .build();

        repository.save(mongoUser);
    }

    private void handleUpdate(JsonNode after) {
        if (after == null) {
            logger.warn("After payload is null during update operation");
            return;
        }

        Long userId = after.get("user_id").asLong();
        Optional<MyChannelInfoViewModel> optionalUser = repository.findById(userId);

        if (optionalUser.isPresent()) {
            MyChannelInfoViewModel existingUser = optionalUser.get();

            MyChannelInfoViewModel updatedUser = MyChannelInfoViewModel.builder()
                    .id(existingUser.getId()) // 유지
                    .name(after.get("user_name").asText())
                    .userEmail(after.get("user_email").asText())
                    .location(after.has("user_location") ? after.get("user_location").asText() : existingUser.getLocation())
                    .birthdate(after.has("birthday") ? formatJoinDate(after, "birthday") : existingUser.getBirthdate())
                    .joinedDate(after.has("join_date") ? formatJoinDate(after, "join_date") : existingUser.getJoinedDate())
                    .gender(after.has("user_gender") ? after.get("user_gender").asText() : existingUser.getGender())
                    .points(new BigDecimal(Base64.getDecoder().decode(after.get("user_point").asText())[0] & 0xFF))
                    .tier_level(after.get("tier_level").asInt())
                    .transactions(existingUser.getTransactions()) // 유지
                    .profitRate(existingUser.getProfitRate()) // 유지
                    .positionValue(existingUser.getPositionValue()) // 유지
                    .tradeCount(existingUser.getTradeCount()) // 유지
                    .build();

            repository.save(updatedUser);
        } else {
            logger.warn("User not found for ID: {}", userId);
        }
    }

    private void handleDelete(JsonNode before) {
        if (before == null) {
            logger.warn("Before payload is null during delete operation");
            return;
        }

        Long userId = before.get("user_id").asLong();
        repository.deleteById(userId);
    }
}
