package com.outsider.masterofpredictionbackend.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class KafkaTemplateFlushAspect {

    @AfterReturning("execution(* org.springframework.kafka.core.KafkaTemplate.send(..))")
    public void afterKafkaSend() {
        // KafkaTemplate을 ApplicationContextProvider를 통해 가져옴
        KafkaTemplate<?, ?> kafkaTemplate = ApplicationContextProvider.getBean(KafkaTemplate.class);
        kafkaTemplate.flush();  // send 후 자동으로 flush 실행
        System.out.println("KafkaTemplate send() called, automatically flushed.");
    }


}
