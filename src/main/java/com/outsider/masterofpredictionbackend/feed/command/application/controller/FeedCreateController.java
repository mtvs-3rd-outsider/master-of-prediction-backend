package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedCreateController {
    private final FeedFacadeService feedFacadeService;
    private final ObjectMapper objectMapper;

    public FeedCreateController(FeedFacadeService feedFacadeService, ObjectMapper objectMapper) {
        this.feedFacadeService = feedFacadeService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseMessage> createFeed(
            @RequestPart("feedData") String feedDataJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "youtubeUrls", required = false) List<String> youtubeUrls) {
        try {
            FeedCreateDTO feedCreateDTO = objectMapper.readValue(feedDataJson, FeedCreateDTO.class);
            Long feedId = feedFacadeService.createFeed(feedCreateDTO, files, youtubeUrls);
            System.out.println(youtubeUrls);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("피드가 성공적으로 생성되었습니다.", feedId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("피드 생성에 실패했습니다: " + e.getMessage(), null));
        }
    }
}