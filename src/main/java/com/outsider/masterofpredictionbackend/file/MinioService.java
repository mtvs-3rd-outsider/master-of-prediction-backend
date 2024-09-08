package com.outsider.masterofpredictionbackend.file;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static java.io.File.separator;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;
    private static final char separator = '_';
    public String uploadFile(MultipartFile file) throws Exception {
        // 버킷이 존재하는지 확인하고, 존재하지 않으면 생성
        StringBuilder sb = new StringBuilder();

        sb.append(UUID.randomUUID());


        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // 파일 업로드
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(sb.toString()).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        return sb.toString();
    }


    public String getFileUrl(String filename) throws Exception {
        // Pre-signed URL 생성
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(filename)
                        .expiry(24 * 60 * 60) // 24시간 유효
                        .build());
    }
}
