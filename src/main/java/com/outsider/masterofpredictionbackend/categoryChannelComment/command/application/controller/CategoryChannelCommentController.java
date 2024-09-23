package com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.controller;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentAddRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentDeleteRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.service.CategoryChannelCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/category-channels/comment")
@RequiredArgsConstructor
@Tag(name = "카테고리 채널 댓글 API", description = "카테고리 채널 댓글 API")
public class CategoryChannelCommentController {

    private final CategoryChannelCommentService categoryChannelCommentService;

    @PostMapping
    public ResponseEntity<Void> addComment(@Valid @RequestBody CategoryChannelCommentAddRequestDTO request) {

        Long commentId = categoryChannelCommentService.addComment(request);

        return ResponseEntity.created(URI.create("/api/v1/category-channels/comment/"+ commentId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable(name = "id")Long id,
                                              @Valid @RequestBody CategoryChannelCommentUpdateRequestDTO request
    ) {

        request.setCommentId(id);
        categoryChannelCommentService.updateComment(request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "id")Long id,
                                              @Valid @RequestBody CategoryChannelCommentDeleteRequestDTO request
    ) {

        request.setCommentId(id);
        categoryChannelCommentService.deleteComment(request);

        return ResponseEntity.noContent().build();
    }
}