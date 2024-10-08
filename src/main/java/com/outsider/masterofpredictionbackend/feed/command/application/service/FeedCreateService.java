package com.outsider.masterofpredictionbackend.feed.command.application.service;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedCreateDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.*;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;

import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalFileService;
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

    @Autowired
    public FeedCreateService(FeedRepository feedRepository,
                             ExternalFileService externalFileService,
                             DTOConverterFacade converterFacade) {
        this.feedRepository = feedRepository;
        this.externalFileService = externalFileService;
        this.converterFacade = converterFacade;
    }

    @Transactional
    public Long createFeed(FeedCreateDTO feedCreateDTO, List<MultipartFile> files, List<String> youtubeUrls) throws Exception {

        List<String> fileUrls = externalFileService.uploadFiles(files);
        feedCreateDTO.setMediaFileUrls(fileUrls);
        feedCreateDTO.setYoutubeUrls(youtubeUrls);

        Feed feed =  converterFacade.toEntity(feedCreateDTO);
        Feed savedFeed = feedRepository.save(feed);
        return savedFeed.getId();
    }


}