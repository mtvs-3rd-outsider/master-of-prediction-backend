package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.controller;

import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoQueryModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/my-channel")
@Slf4j
public class MyChannelInfoController {

    private final MyChannelInfoRepository myChannelInfoRepository;

    // SLF4J 로거 선언

    public MyChannelInfoController(MyChannelInfoRepository myChannelInfoRepository) {
        this.myChannelInfoRepository = myChannelInfoRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MyChannelInfoQueryModel> getUserData(@PathVariable Long userId) {
        // 레포지토리를 사용하여 데이터베이스에서 사용자 데이터를 가져옵니다.
        return myChannelInfoRepository.findById(userId)
                .map(userData -> {
                    // 반환되는 데이터를 로그로 출력
                    log.info("Found user data: {}", userData);
                    return ResponseEntity.ok(userData);
                })
                .orElseGet(() -> {
                    log.warn("User data not found for ID: {}", userId);
                    return ResponseEntity.notFound().build();
                });
    }
}
