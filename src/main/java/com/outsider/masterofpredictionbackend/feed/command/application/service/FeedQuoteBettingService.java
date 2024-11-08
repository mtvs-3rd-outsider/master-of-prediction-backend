//package com.outsider.masterofpredictionbackend.feed.command.application.service;
//
//import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
//import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
//import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
//import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
//import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.QuoteBetting;
//import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
//import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
//import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
//import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
//import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
//import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
//import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalFileService;
//import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
//import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
//import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
//import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class FeedQuoteBettingService {
//    private final FeedRepository feedRepository;
//    private final BettingProductRepository bettingProductRepository;
//    private final ExternalFileService externalFileService;
//    private final ExternalLikeService externalLikeService;
//    private final ExternalUserService externalUserService;
//
//    public Long quoteBetting(Long bettingId, FeedCreateDTO feedCreateDTO, Long userId,
//                             List<MultipartFile> files, List<String> youtubeUrls) throws Exception {
//        // 원본 배팅 조회
//        BettingProduct originalBetting = bettingProductRepository.findById(bettingId)
//                .orElseThrow(() -> new EntityNotFoundException("Betting not found with id: " + bettingId));
//
//        // 배팅 작성자의 최신 정보 조회
//        UserDTO bettingUser = null;
//        if (originalBetting.getUserId() != null) {
//            bettingUser = externalUserService.getUser(originalBetting.getUserId());
//        }
//
//        // 새로운 피드 생성
//        Feed newFeed = new Feed();
//        newFeed.setAuthorType(AuthorType.USER);
//        newFeed.setContent(feedCreateDTO.getContent());
//        newFeed.setCreatedAt(LocalDateTime.now());
//        newFeed.setShortAt(LocalDateTime.now());
//        newFeed.setUser(new User(userId));
//        newFeed.setChannel(feedCreateDTO.getChannel());
//        newFeed.setIsQuoteBetting(true);
//
//        // QuoteBetting 정보 설정
//        QuoteBetting quoteBetting = new QuoteBetting(
//                originalBetting.getId(),
//                originalBetting.getTitle(),
//                bettingUser != null ? bettingUser.getUserName() : "",
//                bettingUser != null ? bettingUser.getDisplayName() : "",
//                bettingUser != null ? bettingUser.getUserImg() : "",
//                bettingUser != null ? bettingUser.getTier().toString() : "",
//                originalBetting.getBlindName(),
//                new ArrayList<>()  // 빈 이미지 리스트로 설정
//        );
//        newFeed.setQuoteBetting(quoteBetting);
//
//        // 미디어 파일 처리
//        if (files != null && !files.isEmpty()) {
//            List<String> fileUrls = externalFileService.uploadFiles(files);
//            List<MediaFile> mediaFiles = fileUrls.stream()
//                    .map(url -> new MediaFile(url, newFeed))
//                    .collect(Collectors.toList());
//            newFeed.setMediaFiles(mediaFiles);
//        }
//
//        // YouTube URL 처리
//        if (youtubeUrls != null && !youtubeUrls.isEmpty()) {
//            List<YouTubeVideo> youTubeVideos = youtubeUrls.stream()
//                    .map(url -> new YouTubeVideo(url, newFeed))
//                    .collect(Collectors.toList());
//            newFeed.setYoutubeVideos(youTubeVideos);
//        }
//
//        // 피드 저장 및 좋아요 카운트 초기화
//        Feed savedFeed = feedRepository.save(newFeed);
//        LikeCountIdDTO likeCountIdDTO = new LikeCountIdDTO(savedFeed.getId(), LikeType.FEED);
//        externalLikeService.saveLikeCount(likeCountIdDTO);
//
//        return savedFeed.getId();
//    }
//
//    public void cancelQuoteBetting(Long feedId, Long userId) {
//        // 인용된 피드 조회
//        Feed quotedFeed = feedRepository.findById(feedId)
//                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));
//
//        // 배팅 인용 피드인지 확인
//        if (!quotedFeed.getIsQuoteBetting()) {
//            throw new IllegalStateException("This feed is not a betting quote.");
//        }
//
//        // 작성자 본인인지 확인
//        if (!quotedFeed.getUser().getUserId().equals(userId)) {
//            throw new AccessDeniedException("Only the creator of the quote can cancel it.");
//        }
//
//        // 인터랙션 확인
//        if (hasInteractions(quotedFeed)) {
//            throw new IllegalStateException("Cannot cancel quote as it already has interactions");
//        }
//
//        // 인용 피드 삭제
//        feedRepository.delete(quotedFeed);
//    }
//
//    private boolean hasInteractions(Feed feed) {
//        return feed.getLikesCount() > 0 ||
//                feed.getCommentsCount() > 0 ||
//                feed.getShareCount() > 0 ||
//                feed.getViewCount() > 10;
//    }
//}