package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedUpdateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
public class FeedUpdateController {
    private final FeedFacadeService feedFacadeService;

    public FeedUpdateController(FeedFacadeService feedFacadeService) {
        this.feedFacadeService = feedFacadeService;
    }

    @PutMapping("/{feedId}")
    public ResponseEntity<ResponseMessage> updateFeed(
            @PathVariable Long feedId,
            @RequestPart("feedData") FeedUpdateDTO feedUpdateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            feedFacadeService.updateFeed(feedId, feedUpdateDTO, files);
            return ResponseEntity.ok(new ResponseMessage("피드가 성공적으로 수정되었습니다."));
        } catch (AccessDeniedException e) {  // 또는 UnauthorizedException
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("피드 수정에 실패했습니다: " + e.getMessage()));
        }
    }
}