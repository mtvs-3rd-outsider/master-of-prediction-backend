package com.outsider.masterofpredictionbackend.mychannel.command.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.betting.query.repository.UserQueryRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.event.ChannelSubscriptionEvent;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.Duration;
@Service
public class ChannelHandler {

    private final ObjectMapper objectMapper;
    private final MyChannelCommandRepository channelRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    public ChannelHandler(ObjectMapper objectMapper, MyChannelCommandRepository userRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.channelRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
// UserService.java

    @KafkaListener(topics = "user-channel-validation-request-channel-part", groupId = "channel-service-group")
    public void handleValidationRequest(String message, Acknowledgment ack) {
        try {
            // JSON 문자열을 ChannelSubscriptionEvent 객체로 역직렬화
            ChannelSubscriptionEvent event = objectMapper.readValue(message, ChannelSubscriptionEvent.class);
            ChannelSubscribeRequestDTO dto = event.getDto();
            boolean userExists = channelRepository.existsById(dto.getChannelId());
            // 검증 결과 이벤트 발행
            ChannelSubscriptionEvent responseEvent = new ChannelSubscriptionEvent(
                    dto,
                    userExists
            );
            String eventJson = objectMapper.writeValueAsString(responseEvent);
            kafkaTemplate.send("user-channel-validation-response-channel-part", eventJson);
            ack.acknowledge();
        } catch (Exception e) {
            ack.nack(Duration.ofSeconds(1));
        }
    }
}
