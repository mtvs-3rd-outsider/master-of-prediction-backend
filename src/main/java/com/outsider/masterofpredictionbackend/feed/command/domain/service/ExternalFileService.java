package com.outsider.masterofpredictionbackend.feed.command.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExternalFileService {

    String uploadFile(MultipartFile file) throws Exception;

    List<String> uploadFiles(List<MultipartFile> files) throws Exception;
}