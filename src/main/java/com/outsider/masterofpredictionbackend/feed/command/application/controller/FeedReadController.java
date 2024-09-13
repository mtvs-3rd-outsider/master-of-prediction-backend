package com.outsider.masterofpredictionbackend.feed.command.application.controller;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedFacadeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedReadController {
    private final FeedFacadeService feedFacadeService;

    public FeedReadController(FeedFacadeService feedFacadeService) {
        this.feedFacadeService = feedFacadeService;
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponseDTO> getFeed(@PathVariable Long feedId) {
        try {
            FeedResponseDTO feed = feedFacadeService.getFeed(feedId);
            System.out.println(11);
            return ResponseEntity.ok(feed);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}