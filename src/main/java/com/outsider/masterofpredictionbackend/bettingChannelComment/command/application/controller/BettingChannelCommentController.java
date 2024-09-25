package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.controller;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentAddRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.service.BettingCommentService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/betting-products/comments")
@Tag(name = "배팅 댓글 API", description = "배팅 댓글 API")
public class BettingChannelCommentController {

    private final BettingCommentService bettingCommentService;

    @PostMapping
    public ResponseEntity<Void> addComment(@Valid @RequestBody BettingCommentAddRequestDTO request,
                                           @UserId CustomUserInfoDTO userInfoDTO){
        Long commentId = bettingCommentService.addComment(request, userInfoDTO);
        return ResponseEntity.created(URI.create("api/v1/betting-products/comments/"+commentId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id,
                                              @Valid @RequestBody BettingCommentUpdateRequestDTO request,
                                              @UserId CustomUserInfoDTO userInfoDTO){
        request.setCommentId(id);
        bettingCommentService.updateComment(request, userInfoDTO);

        return ResponseEntity.created(URI.create("api/v1/betting-products/comments/"+id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                              @UserId CustomUserInfoDTO userInfoDTO){
        bettingCommentService.deleteComment(id, userInfoDTO);

        return ResponseEntity.noContent().build();
    }
}
