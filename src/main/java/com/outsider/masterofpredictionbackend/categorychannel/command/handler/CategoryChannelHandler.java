package com.outsider.masterofpredictionbackend.categorychannel.command.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.event.ChannelSubscriptionEvent;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class CategoryChannelHandler {

    private final ObjectMapper objectMapper;
    private final CategoryChannelRepository channelRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    public CategoryChannelHandler(ObjectMapper objectMapper, CategoryChannelRepository channelRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.channelRepository = channelRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
// UserService.java

    @KafkaListener(topics = "category-channel-validation-request-channel-part", groupId = "channel-service-group")
    public void handleValidationRequest(String message, Acknowledgment ack) {
        try {
            ack.acknowledge();
            // JSON 문자열을 ChannelSubscriptionEvent 객체로 역직렬화
            ChannelSubscriptionEvent event = objectMapper.readValue(message, ChannelSubscriptionEvent.class);
            ChannelSubscribeRequestDTO dto = event.getDto();
            boolean channelExists = channelRepository.existsById(dto.getChannelId());
            // 검증 결과 이벤트 발행
            ChannelSubscriptionEvent responseEvent = new ChannelSubscriptionEvent(
                    dto,
                    channelExists
            );
            // 로그 기록
            log.info("Validation request processed for channelId: {}, getChannelId: {}, channelExists: {}",
                    dto.getChannelId(), dto.getChannelId(), channelExists);

            String eventJson = objectMapper.writeValueAsString(responseEvent);
            kafkaTemplate.send("user-channel-validation-response-channel-part", eventJson);


            // 처리 확인 로그
            log.info("Validation response sent for channelId: {}, getChannelId: {}",
                    dto.getChannelId(), dto.getChannelId());
        } catch (Exception e) {
            log.error("Error occurred while processing validation request: {}", e.getMessage(), e);
        }
    }
}
