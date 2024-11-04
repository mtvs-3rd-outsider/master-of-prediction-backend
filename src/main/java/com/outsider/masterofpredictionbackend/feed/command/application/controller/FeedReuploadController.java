package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedReuploadService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedReuploadController {
    private final FeedReuploadService feedReuploadService;

    public FeedReuploadController(FeedReuploadService feedReuploadService) {
        this.feedReuploadService = feedReuploadService;
    }

    @PostMapping("/{feedId}/reupload")
    public ResponseEntity<ResponseMessage> reuploadFeed(
            @PathVariable Long feedId,
            @UserId CustomUserInfoDTO userInfoDTO) {
        try {
            feedReuploadService.reuploadFeed(feedId, userInfoDTO.getUserId());
            return ResponseEntity.ok(new ResponseMessage("피드가 성공적으로 재업로드되었습니다.", feedId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("피드 재업로드에 실패했습니다: " + e.getMessage(), feedId));
        }
    }

    @DeleteMapping("/{feedId}/reupload")
    public ResponseEntity<ResponseMessage> removeReupload(
            @PathVariable Long feedId,
            @UserId CustomUserInfoDTO userInfoDTO) {
        try {
            feedReuploadService.removeReupload(feedId, userInfoDTO.getUserId());
            return ResponseEntity.ok(new ResponseMessage("재업로드가 성공적으로 취소되었습니다.", feedId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("재업로드 취소에 실패했습니다: " + e.getMessage(), feedId));
        }
    }
}
