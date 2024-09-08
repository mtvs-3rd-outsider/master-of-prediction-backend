package com.outsider.masterofpredictionbackend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {


    @Bean
    public NewTopic feedTopic() {
        return new NewTopic("feed.created", 1, (short) 1);
    }

    @Bean
    public NewTopic commentTopic() {
        return new NewTopic("comment.created", 1, (short) 1);
    }

    @Bean
    public NewTopic userTopic() {
        return new NewTopic("user.registered", 1, (short) 1);
    }
}