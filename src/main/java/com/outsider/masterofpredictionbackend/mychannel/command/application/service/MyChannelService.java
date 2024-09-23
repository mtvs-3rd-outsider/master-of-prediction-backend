package com.outsider.masterofpredictionbackend.mychannel.command.application.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Bio;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Website;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.MY_CHANNEL_UPDATE_TOPIC;

/**
 * MyChannelService는 채널의 정보를 관리하는 기능을 제공합니다.
 * 채널이 없을 경우 새로 등록하고, 이미 존재하면 업데이트합니다.
 */
@Service
@Slf4j
public class MyChannelService {

    private final MyChannelCommandRepository myChannelRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MyChannelService(MyChannelCommandRepository myChannelRepository,
                            KafkaTemplate<String, String> kafkaTemplate,
                            ObjectMapper objectMapper) {
        this.myChannelRepository = myChannelRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 주어진 채널 ID에 따라 채널을 등록하거나 업데이트합니다.
     * 채널이 없으면 새로 등록하고, 이미 존재하면 업데이트합니다.
     * 업데이트 후 Kafka로 메시지를 전송합니다.
     */
    @Transactional
    public void saveOrUpdateMyChannel(MyChannelInfoUpdateRequestDTO dto) {
        // 채널이 이미 존재하는지 확인
        MyChannel myChannel = myChannelRepository.findById(dto.getUserId())
                .orElse(null);

        if (myChannel == null) {
            // 채널이 없으면 새로 등록
            myChannel = new MyChannel(
                    dto.getUserId(),
                    new Bio(dto.getBio()),
                    new Website(dto.getWebsite())
            );
            myChannelRepository.save(myChannel);
            log.info("Channel registered: {}", myChannel.getId());
        } else {
            // 채널이 있으면 업데이트
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
            log.info("Channel updated: {}", myChannel.getId());
        }
    }

    /**
     * Kafka로부터 채널 정보 업데이트 메시지를 수신하여 처리하는 리스너 메서드입니다.
     * 수신된 메시지는 DTO로 변환된 후 saveOrUpdateMyChannel 메서드를 호출하여 처리됩니다.
     */
    @KafkaListener(topics = MY_CHANNEL_UPDATE_TOPIC)
    @Transactional
    public void consume(String message) throws JsonProcessingException {
        // JSON 문자열을 DTO로 변환
        MyChannelInfoUpdateRequestDTO dto = objectMapper.readValue(message, MyChannelInfoUpdateRequestDTO.class);

        // DTO를 사용하여 채널 정보 등록 또는 업데이트
        saveOrUpdateMyChannel(dto);

        log.info("Processed channel from Kafka message: {}", dto);
    }
}
