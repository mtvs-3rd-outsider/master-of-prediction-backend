package com.outsider.masterofpredictionbackend.mychannel.command.application.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateChannelUserCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.service.UpdateMyChannelFollowerCountService;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.UpdateFollowMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.UPDATE_FOLLOWER_COUNT_TOPIC;

@Service
@Slf4j
public class UpdateFollowerCountConsumerImpl {

    private final ObjectMapper objectMapper;
    private final UpdateMyChannelFollowerCountService updateMyChannelFollowerCountService;

    public UpdateFollowerCountConsumerImpl(ObjectMapper objectMapper,
                                           UpdateMyChannelFollowerCountService updateMyChannelFollowerCountService) {
        this.objectMapper = objectMapper;
        this.updateMyChannelFollowerCountService = updateMyChannelFollowerCountService;
    }

    /**
     * Kafka로부터 팔로워 업데이트 메시지를 수신하여 처리하는 메서드.
     * 수신된 메시지는 DTO로 변환된 후 처리됩니다.
     */
    @KafkaListener(topics = UPDATE_FOLLOWER_COUNT_TOPIC, groupId = "follower-update-group")
    public void consume(String message, Acknowledgment ack) {
        try {
            // JSON 문자열을 UpdateFollowMessage 객체로 변환
            UpdateFollowMessage updateFollowMessage = objectMapper.readValue(message, UpdateFollowMessage.class);

            // 수신된 메시지 처리
            log.info("Consumed follower update message: {}", updateFollowMessage);

            // 팔로워 업데이트를 위한 DTO 생성
            UpdateChannelUserCountDTO dto = new UpdateChannelUserCountDTO(
                    updateFollowMessage.getChannelId(),
                    updateFollowMessage.isPlus()
            );

            // 팔로워 수 증가 또는 감소
            updateMyChannelFollowerCountService.updateFollowerMyChannel(dto);

            // 메시지 처리 성공 시 ACK
            ack.acknowledge();

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize Kafka message: {}", message, e);
            // JSON 파싱 실패 시 NACK - 1초 후 재처리
        } catch (IllegalArgumentException e) {
            log.error("Invalid Channel ID in message: {}", e.getMessage());
            // Channel ID 오류 발생 시 NACK - 1초 후 재처리
        } catch (Exception e) {
            log.error("Unexpected error occurred while processing message: {}", message, e);
            // 기타 예외 발생 시 NACK - 1초 후 재처리
        }
    }
}
