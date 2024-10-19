package com.outsider.masterofpredictionbackend.like.command.application.controller;

import com.outsider.masterofpredictionbackend.common.ResponseMessage;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.application.service.LikeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/like")
public class FeedLikeController {

    public final LikeService likeService;

    public FeedLikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> like(@RequestBody LikeDTO likeDTO) {
        if(likeService.isLike(likeDTO)){
            return ResponseEntity.ok(new ResponseMessage("좋아요"));
        }else {
            return ResponseEntity.ok(new ResponseMessage("좋아요 취소"));
        }
    }
}
