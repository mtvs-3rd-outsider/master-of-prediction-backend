package com.outsider.masterofpredictionbackend.feed.command.application.controller;


import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedUpdateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feeds")
public class FeedUpdateController {
    private final FeedFacadeService feedFacadeService;

    public FeedUpdateController(FeedFacadeService feedFacadeService) {
        this.feedFacadeService = feedFacadeService;
    }

    // Feed 수정 엔드포인트
    @PutMapping("/{feedId}")
    public ResponseEntity<ResponseMessage> updateFeed(@PathVariable Long feedId, @RequestBody FeedUpdateDTO feedUpdateDTO) {
        try {
            FeedResponseDTO feed = feedFacadeService.getFeed(feedId);
            if(feed.getAuthorType()==AuthorType.GUEST){
                if(!(feed.getGuest().getGuestId().equals(feedUpdateDTO.getGuest().getGuestId()))||!(feed.getGuest().getGuestPassword().equals(feedUpdateDTO.getGuest().getGuestPassword()))){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseMessage("아이디 혹은 비밀번호가 틀립니다."));
                }
            }
            feedFacadeService.updateFeed(feedId, feedUpdateDTO);
            return ResponseEntity.ok(new ResponseMessage("피드가 성공적으로 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("피드 수정에 실패했습니다: " + e.getMessage()));
        }
    }
}