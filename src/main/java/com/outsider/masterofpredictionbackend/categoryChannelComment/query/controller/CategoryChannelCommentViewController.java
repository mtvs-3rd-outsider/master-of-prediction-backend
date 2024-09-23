package com.outsider.masterofpredictionbackend.categoryChannelComment.query.controller;
import com.outsider.masterofpredictionbackend.categoryChannelComment.query.dto.CategoryChannelCommentViewDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.query.service.CategoryChannelCommentViewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category-channels/comment/view")
@Tag(name = "카테고리 채널 댓글 조회 API", description = "카테고리 채널 댓글 조회 API")
public class CategoryChannelCommentViewController {

    private final CategoryChannelCommentViewService viewService;

    @GetMapping("/{channelId}")
    public ResponseEntity<List<CategoryChannelCommentViewDTO>> getCommentsByChannelId(@PathVariable(name = "channelId")Long channelId) {

        List<CategoryChannelCommentViewDTO> response = viewService.getCommentsByChannelId(channelId);

        return ResponseEntity.ok(response);
    }
}
