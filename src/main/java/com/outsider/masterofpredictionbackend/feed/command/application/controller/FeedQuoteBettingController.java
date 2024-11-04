//package com.outsider.masterofpredictionbackend.feed.command.application.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.outsider.masterofpredictionbackend.common.ResponseMessage;
//import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
//import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedQuoteBettingService;
//import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
//import com.outsider.masterofpredictionbackend.util.UserId;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/feeds")
//@RequiredArgsConstructor
//public class FeedQuoteBettingController {
//    private final FeedQuoteBettingController feedQuoteBettingService;
//    private final ObjectMapper objectMapper;
//
//
//    @PostMapping(value = "/{bettingId}/quote-betting", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseMessage> quoteBetting(
//            @PathVariable Long bettingId,
//            @RequestPart("feedData") String feedDataJson,
//            @RequestPart(value = "files", required = false) List<MultipartFile> files,
//            @RequestParam(value = "youtubeUrls", required = false) List<String> youtubeUrls,
//            @UserId CustomUserInfoDTO userInfoDTO) {
//        try {
//            FeedCreateDTO feedCreateDTO = objectMapper.readValue(feedDataJson, FeedCreateDTO.class);
//            Long quotedFeedId = feedQuoteBettingService.quoteBetting(
//                    bettingId, feedCreateDTO, userInfoDTO.getUserId(), files, youtubeUrls
//            );
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new ResponseMessage("배팅이 성공적으로 인용되었습니다.", quotedFeedId));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseMessage("배팅 인용에 실패했습니다: " + e.getMessage()));
//        }
//    }
//
//    @DeleteMapping("/{feedId}/quote-betting")
//    public ResponseEntity<ResponseMessage> cancelQuoteBetting(
//            @PathVariable Long feedId,
//            @UserId CustomUserInfoDTO userInfoDTO) {
//        try {
//            feedQuoteBettingService.cancelQuoteBetting(feedId, userInfoDTO.getUserId());
//            return ResponseEntity.ok(new ResponseMessage("배팅 인용이 성공적으로 취소되었습니다."));
//        } catch (IllegalStateException e) {
//            return ResponseEntity.badRequest()
//                    .body(new ResponseMessage(e.getMessage()));
//        } catch (AccessDeniedException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(new ResponseMessage("인용 취소 권한이 없습니다."));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseMessage("배팅 인용 취소에 실패했습니다: " + e.getMessage()));
//        }
//    }
//}