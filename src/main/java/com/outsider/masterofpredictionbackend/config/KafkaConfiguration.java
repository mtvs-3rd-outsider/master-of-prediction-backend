package com.outsider.masterofpredictionbackend.config;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Configuration
@Order(1)
public class KafkaConfiguration {



    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000); // 요청 타임아웃
        props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500); // 재시도 간격
        // 수동 commit을 위한 설정 (ACK를 MANUAL 모드로 설정하는 경우)

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); // 기본값 500
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "600000"); // 기본값 300000 (5분)
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1"); // 기본값은 1 byte
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "500"); // 기본값은 500 ms
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000"); // 기본값 10000 (10초)
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "10000"); // 기본값 3000 (3초)
        return props;
    }

    @Bean()
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // ackMode 설정 (필요한 ackMode로 설정)
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE); // BATCH 모드 예시
        // ackMode 출력
        log.info("Kafka Listener AckMode: {}", factory.getContainerProperties().getAckMode());

        return factory;
    }

}