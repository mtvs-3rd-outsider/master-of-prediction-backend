package com.outsider.masterofpredictionbackend.betting.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BettingProductOptionDTO {

    private Long id;

    private String content;

    private MultipartFile image;

    public BettingProductOptionDTO(String content, MultipartFile image) {
        this.content = content;
        this.image = image;
    }
}
