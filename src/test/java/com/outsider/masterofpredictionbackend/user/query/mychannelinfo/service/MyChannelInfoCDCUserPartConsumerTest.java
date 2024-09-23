package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MyChannelInfoCDCUserPartConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MyChannelInfoRepository myChannelInfoRepository;

    @BeforeEach
    void setUp() {
        // 필요시 추가 설정 또는 초기화 작업
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 데이터 정리
        myChannelInfoRepository.deleteAll();
    }
    @DisplayName("카프카 메시지 네 개 보내기 테스트")
    @Test
    void testKafkaMessageProcessing() throws Exception {
        // Given
        String topic = "dbserver1.forecasthub.user";

        // 네 개의 유저 생성 메시지
        String userCreateMessage1 = "{ \"payload\": { \"op\": \"c\", \"after\": { \"user_id\": 1, \"name\": \"John Doe\" } } }";
        String userCreateMessage2 = "{ \"payload\": { \"op\": \"c\", \"after\": { \"user_id\": 2, \"name\": \"Jane Smith\" } } }";
        String userCreateMessage3 = "{ \"payload\": { \"op\": \"c\", \"after\": { \"user_id\": 3, \"name\": \"Alice Johnson\" } } }";
        String userCreateMessage4 = "{ \"payload\": { \"op\": \"c\", \"after\": { \"user_id\": 4, \"name\": \"Bob Lee\" } } }";

        // When
        // 네 개의 Kafka 메시지 전송
        kafkaTemplate.send(topic, userCreateMessage1);
        kafkaTemplate.send(topic, userCreateMessage2);
        kafkaTemplate.send(topic, userCreateMessage3);
        kafkaTemplate.send(topic, userCreateMessage4);

        // 메시지 소비 대기 시간 (인덱싱이 완료될 때까지 기다림)
        Thread.sleep(2000);

        // Then
        // 첫 번째 메시지가 처리되어 MyChannelInfoQueryModel에 저장되었는지 확인
        assertThat(myChannelInfoRepository.findById(1L)).isPresent();

        // 두 번째 메시지가 처리되어 MyChannelInfoQueryModel에 저장되었는지 확인
        assertThat(myChannelInfoRepository.findById(2L)).isPresent();

        // 세 번째 메시지가 처리되어 MyChannelInfoQueryModel에 저장되었는지 확인
        assertThat(myChannelInfoRepository.findById(3L)).isPresent();

        // 네 번째 메시지가 처리되어 MyChannelInfoQueryModel에 저장되었는지 확인
        assertThat(myChannelInfoRepository.findById(4L)).isPresent();
    }



}
