package com.outsider.masterofpredictionbackend.betting.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
public class BettingProductOptionDTO {

    private Long id;

    @NotBlank(message = "content is required")
    private String content;

    private MultipartFile imgUrl;

}
