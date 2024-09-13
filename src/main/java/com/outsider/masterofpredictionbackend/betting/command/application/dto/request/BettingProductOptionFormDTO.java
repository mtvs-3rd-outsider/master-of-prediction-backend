package com.outsider.masterofpredictionbackend.betting.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BettingProductOptionFormDTO {

    @NotNull(message = "options_image is required")
    private List<MultipartFile>options_image;

    @NotNull(message = "options_content is required")
    private List<String> options_content;
}
