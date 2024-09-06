package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.controller;

import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto.MyChannelInfoViewModel;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.repository.MyChannelInfoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/my-channel")
public class MyChannelInfoController {

    private final MyChannelInfoRepository myChannelInfoRepository;

    // 생성자 주입을 사용하여 레포지토리를 주입합니다.
    public MyChannelInfoController(MyChannelInfoRepository myChannelInfoRepository) {
        this.myChannelInfoRepository = myChannelInfoRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MyChannelInfoViewModel> getUserData(@PathVariable Long userId) {
        // 레포지토리를 사용하여 데이터베이스에서 사용자 데이터를 가져옵니다.
        Optional<MyChannelInfoViewModel> myChannelInfoViewModel= myChannelInfoRepository.findById(userId);
        return myChannelInfoRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
