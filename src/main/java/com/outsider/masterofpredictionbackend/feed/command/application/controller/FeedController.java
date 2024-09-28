package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedUpdateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedController {
    private final FeedFacadeService feedFacadeService;
    private final ObjectMapper objectMapper;

    public FeedController(FeedFacadeService feedFacadeService, ObjectMapper objectMapper) {
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
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("피드가 성공적으로 생성되었습니다.", feedId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("피드 생성에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponseDTO> getFeed(@PathVariable Long feedId) {
        try {
            FeedResponseDTO feed = feedFacadeService.getFeed(feedId);
            return ResponseEntity.ok(feed);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> updateFeed(
            @PathVariable Long feedId,
            @RequestPart("feedData") FeedUpdateDTO feedUpdateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "youtubeUrls", required = false) List<String> youtubeUrls) {
        try {
            feedFacadeService.updateFeed(feedId, feedUpdateDTO, files, youtubeUrls);
            return ResponseEntity.ok(new ResponseMessage("피드가 성공적으로 수정되었습니다."));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("피드 수정에 실패했습니다: " + e.getMessage()));
        }
    }

    // Feed 삭제 엔드포인트
    @DeleteMapping("/{feedId}")
    public ResponseEntity<ResponseMessage> deleteFeed(@PathVariable Long feedId) {
        try {
            feedFacadeService.deleteFeed(feedId);
            return ResponseEntity.ok(new ResponseMessage("피드가 성공적으로 삭제되었습니다.", feedId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("피드 삭제에 실패했습니다: " + e.getMessage(), feedId));
        }
    }
}