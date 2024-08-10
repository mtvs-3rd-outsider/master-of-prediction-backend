package com.outsider.masterofpredictionbackend.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class ImageValidator {

    private static final long MAX_FILE_SIZE = 3 * 1024 * 1024; // 3MB in bytes
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/png", "image/jpeg", "image/gif");

    public static void validateImageFile(MultipartFile file) {
        validateFileSize(file);
        validateContentType(file);
        validateImageFormat(file);
    }

    private static void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidImageException("Image size is too big");
        }
    }

    private static void validateContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new InvalidImageException("The file is not a valid image.");
        }
    }

    private static void validateImageFormat(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new InvalidImageException("The file is not a valid image.");
            }
        } catch (IOException e) {
            throw new InvalidImageException("Error occurred while processing the image file.", e);
        }
    }
}
