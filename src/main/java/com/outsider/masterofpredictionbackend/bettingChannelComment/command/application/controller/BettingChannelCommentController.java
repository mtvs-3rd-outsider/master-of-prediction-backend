package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.controller;

import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentAddRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.service.BettingCommentService;
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
    public ResponseEntity<Void> addComment(@Valid @RequestBody BettingCommentAddRequestDTO request){
        Long commentId = bettingCommentService.addComment(request);
        return ResponseEntity.created(URI.create("api/v1/betting-products/comments/"+commentId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id, @Valid @RequestBody BettingCommentUpdateRequestDTO request){
        request.setCommentId(id);
        bettingCommentService.updateComment(request);

        return ResponseEntity.created(URI.create("api/v1/betting-products/comments/"+id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        bettingCommentService.deleteComment(id);

        return ResponseEntity.noContent().build();
    }
}
