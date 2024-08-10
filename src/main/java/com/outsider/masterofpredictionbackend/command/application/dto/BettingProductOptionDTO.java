package com.outsider.masterofpredictionbackend.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BettingProductOptionDTO {

    private Long id;

    @NotBlank(message = "content is required")
    private String content;

    private MultipartFile imgUrl;

}
