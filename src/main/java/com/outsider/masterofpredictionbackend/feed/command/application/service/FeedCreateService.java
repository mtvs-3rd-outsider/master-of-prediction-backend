package com.outsider.masterofpredictionbackend.feed.command.application.service;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedCreateDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.*;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;

import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalFileService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FeedCreateService {
    private final FeedRepository feedRepository;
    private final ExternalFileService externalFileService;
    private final DTOConverterFacade converterFacade;
    private final ExternalLikeService externalLikeService;
    @Autowired
    public FeedCreateService(FeedRepository feedRepository,
                             ExternalFileService externalFileService,
                             DTOConverterFacade converterFacade,
                             ExternalLikeService externalLikeService) {
        this.feedRepository = feedRepository;
        this.externalFileService = externalFileService;
        this.converterFacade = converterFacade;
        this.externalLikeService = externalLikeService;
    }

    @Transactional
    public Long createFeed(FeedCreateDTO feedCreateDTO, List<MultipartFile> files, List<String> youtubeUrls) throws Exception {

        List<String> fileUrls = externalFileService.uploadFiles(files);
        feedCreateDTO.setMediaFileUrls(fileUrls);
        feedCreateDTO.setYoutubeUrls(youtubeUrls);
        Feed feed =  converterFacade.toEntity(feedCreateDTO);
        Feed savedFeed = feedRepository.save(feed);
        LikeCountIdDTO likeCountIdDTO = new LikeCountIdDTO(feed.getId(), LikeType.FEED);
        externalLikeService.saveLikeCount(likeCountIdDTO);
        return savedFeed.getId();
    }


}