package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.controller;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentAddRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.service.BettingCommentService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "배팅 댓글 생성", description = "배팅 댓글을 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> addComment(@UserId CustomUserInfoDTO userInfoDTO,
                                           @Valid @RequestBody BettingCommentAddRequestDTO request){

        Long commentId = bettingCommentService.addComment(request, userInfoDTO);
        return ResponseEntity.created(URI.create("api/v1/betting-products/comments/"+commentId)).build();
    }

    @Operation(summary = "배팅 댓글 수정", description = "배팅 댓글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id,
                                              @Valid @RequestBody BettingCommentUpdateRequestDTO request,
                                              @UserId CustomUserInfoDTO userInfoDTO){
        request.setCommentId(id);
        bettingCommentService.updateComment(request, userInfoDTO);

        return ResponseEntity.created(URI.create("api/v1/betting-products/comments/"+id)).build();
    }

    @Operation(summary = "배팅 댓글 삭제", description = "배팅 댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                              @UserId CustomUserInfoDTO userInfoDTO){
        bettingCommentService.deleteComment(id, userInfoDTO);

        return ResponseEntity.noContent().build();
    }
}
