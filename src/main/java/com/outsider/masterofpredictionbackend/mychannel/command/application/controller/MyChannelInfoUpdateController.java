package com.outsider.masterofpredictionbackend.mychannel.command.application.controller;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.service.MyChannelInfoUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mychannel")
public class MyChannelInfoUpdateController {

    private final MyChannelInfoUpdateService myChannelInfoUpdateService;

    /**
     * MyChannelInfoUpdateController 생성자입니다.
     *
     * @param myChannelInfoUpdateService 채널 정보 업데이트 서비스를 주입
     */
    @Autowired
    public MyChannelInfoUpdateController(MyChannelInfoUpdateService myChannelInfoUpdateService) {
        this.myChannelInfoUpdateService = myChannelInfoUpdateService;
    }

    /**
     * 채널의 정보를 업데이트하는 엔드포인트입니다.
     *
     * @param dto 채널 정보 업데이트 요청 DTO
     * @return 상태 코드 200 OK 또는 404 Not Found
     */
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateMyChannel(@RequestBody MyChannelInfoUpdateRequestDTO dto,@PathVariable Long userId ) {
        try {
            myChannelInfoUpdateService.updateMyChannel(dto);
            return new ResponseEntity<>("Channel updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Channel not found", HttpStatus.NOT_FOUND);
        }
    }
}
