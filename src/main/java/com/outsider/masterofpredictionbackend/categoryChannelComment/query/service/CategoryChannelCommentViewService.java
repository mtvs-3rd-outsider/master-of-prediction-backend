package com.outsider.masterofpredictionbackend.categoryChannelComment.query.service;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.repository.CategoryChannelCommentRepository;
import com.outsider.masterofpredictionbackend.categoryChannelComment.query.dto.CategoryChannelCommentViewDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryChannelCommentViewService {
    private final CategoryChannelCommentRepository repository;

    public List<CategoryChannelCommentViewDTO> getCommentsByChannelId(Long channelId) {
        List<CategoryChannelComment> comments = repository.findAllByChannelId(channelId);

        List<CategoryChannelCommentViewDTO> result = new ArrayList<>();

        /*에그리거트 -> DTO 변환*/
        for (CategoryChannelComment comment : comments) {
            CategoryChannelCommentViewDTO dto = new CategoryChannelCommentViewDTO();

            dto.setCommentId(comment.getId());
            dto.setComment(comment.getContent().getContent());
            dto.setWriter(comment.getWriter().getWriterName());
            dto.setImageUrl(comment.getContent().getImageUrl());
            dto.setWriteAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            result.add(dto);
        }

        return result;
    }
}
