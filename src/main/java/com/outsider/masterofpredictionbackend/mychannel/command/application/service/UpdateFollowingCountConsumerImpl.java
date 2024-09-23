package com.outsider.masterofpredictionbackend.mychannel.command.application.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateChannelUserCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.service.UpdateMyChannelFollowingCountService;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.UpdateFollowMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.UPDATE_FOLLOWING_COUNT_TOPIC;

@Service
@Slf4j
public class UpdateFollowingCountConsumerImpl {

    private final ObjectMapper objectMapper;
    private final UpdateMyChannelFollowingCountService updateMyChannelFollowingCountService;

    public UpdateFollowingCountConsumerImpl(ObjectMapper objectMapper,
                                            UpdateMyChannelFollowingCountService updateMyChannelFollowingCountService) {
        this.objectMapper = objectMapper;
        this.updateMyChannelFollowingCountService = updateMyChannelFollowingCountService;
    }

    /**
     * Kafka로부터 팔로잉 업데이트 메시지를 수신하여 처리하는 메서드.
     * 수신된 메시지는 DTO로 변환된 후 처리됩니다.
     */
    @KafkaListener(topics = UPDATE_FOLLOWING_COUNT_TOPIC, groupId = "following-update-group")
    public void consume(String message) {
        try {
            // JSON 문자열을 UpdateFollowMessage 객체로 변환
            UpdateFollowMessage updateFollowMessage = objectMapper.readValue(message, UpdateFollowMessage.class);

            // 수신된 메시지 처리
            log.info("Consumed following update message: {}", updateFollowMessage);

            // 팔로잉 업데이트를 위한 DTO 생성
            UpdateChannelUserCountDTO dto = new UpdateChannelUserCountDTO(
                    updateFollowMessage.getChannelId(),
                    updateFollowMessage.isPlus()
            );

            // 팔로잉 수 증가 또는 감소
            updateMyChannelFollowingCountService.updateFollowingsMyChannel(dto);

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize Kafka message: {}", message, e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid Channel ID in message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while processing message: {}", message, e);
        }
    }
}
