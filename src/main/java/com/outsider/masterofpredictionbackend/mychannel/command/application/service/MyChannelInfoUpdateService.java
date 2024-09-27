package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Bio;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.DisplayName;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Website;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * 서비스 클래스는 채널의 정보를 업데이트하는 기능을 제공합니다.
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.MY_CHANNEL_UPDATE_TOPIC;

/**
 * 서비스 클래스는 채널의 정보를 업데이트하고 Kafka에 메시지를 전송하는 기능을 제공합니다.
 */
@Service
@Slf4j
public class MyChannelInfoUpdateService {

    private final MyChannelCommandRepository myChannelRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MyChannelInfoUpdateService(MyChannelCommandRepository myChannelRepository,
                                      ObjectMapper objectMapper) {
        this.myChannelRepository = myChannelRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 주어진 채널 ID에 따라 채널의 정보를 업데이트합니다.
     * DTO에 null 값이 아닌 데이터만 업데이트됩니다.
     * 업데이트 후 Kafka로 메시지를 전송합니다.
     */
    @Transactional
    public void updateMyChannel(MyChannelInfoUpdateRequestDTO dto) {
        MyChannel myChannel = myChannelRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));



        if (dto.getBio() != null) {
            myChannel.setBio(new Bio(dto.getBio()));
        }

        if (dto.getWebsite() != null) {
            myChannel.setWebsite(new Website(dto.getWebsite()));
        }
        if (dto.getBannerImg() != null) {
            myChannel.setBannerImg(dto.getBannerImg());
        }
        myChannelRepository.save(myChannel);
    }

    /**
     * Kafka로부터 채널 정보 업데이트 메시지를 수신하여 처리하는 리스너 메서드입니다.
     * 수신된 메시지는 DTO로 변환된 후 updateMyChannel 메서드를 호출하여 처리됩니다.
     */
    @KafkaListener(topics = MY_CHANNEL_UPDATE_TOPIC)
    @Transactional
    public void consume(String message, Acknowledgment ack)  {

        try {
            // JSON 문자열을 DTO로 변환
            MyChannelInfoUpdateRequestDTO dto = objectMapper.readValue(message, MyChannelInfoUpdateRequestDTO.class);

            // DTO를 사용하여 채널 정보 업데이트
            updateMyChannel(dto);

            log.info("Updated channel from Kafka message: {}", dto);

            // 트랜잭션 성공 시 수동으로 메시지 ACK
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Failed to process message: {}", message, e);
            ack.nack(Duration.ofSeconds(1));
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage(), e);

            // 트랜잭션 내에서 발생한 오류에 대해 nack 호출하여 메시지 재처리
            ack.nack(Duration.ofSeconds(1));
        }
    }
}
