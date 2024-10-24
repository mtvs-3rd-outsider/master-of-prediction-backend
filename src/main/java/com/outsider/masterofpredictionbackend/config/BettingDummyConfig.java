package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductAndOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.dto.request.BettingProductOptionDTO;
import com.outsider.masterofpredictionbackend.betting.command.application.service.ProductCommandService;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Profile("dev")
@Configuration
public class BettingDummyConfig {

    private final ProductCommandService productCommandService;
    private final BettingProductRepository bettingProductRepository;
    private final ResourceLoader resourceLoader;

    public BettingDummyConfig(ProductCommandService productCommandService, BettingProductRepository bettingProductRepository, ResourceLoader resourceLoader) {
        this.productCommandService = productCommandService;
        this.bettingProductRepository = bettingProductRepository;
        this.resourceLoader = resourceLoader;
    }

    private List<BettingProductAndOptionDTO> createBettingProductAndOptionDTO() throws IOException {
        String IMAGE_URL = "classpath:static/images";
        String CAT = "/cat.jpg";
        String DOG = "/dog.jpg";

        return List.of(new BettingProductAndOptionDTO(
                "title",
                "content",
                1L,
                1L,
                LocalDateTime.now().withNano(0),
                LocalDate.now().plusMonths(1),
                LocalTime.now(),
                List.of(getImage(IMAGE_URL + CAT), getImage(IMAGE_URL + DOG)),
                false,
                null,
                List.of(new BettingProductOptionDTO(
                        "dog",
                        getImage(IMAGE_URL + DOG)
                ), new BettingProductOptionDTO(
                        "cat",
                        getImage(IMAGE_URL + CAT)
                ))
        ), new BettingProductAndOptionDTO(
                "title",
                "content",
                1L,
                1L,
                LocalDateTime.now().withNano(0),
                LocalDate.now().plusMonths(1),
                LocalTime.now().withNano(0),
                List.of(getImage(IMAGE_URL + CAT), getImage(IMAGE_URL + DOG)),
                true,
                "blindName",
                List.of(new BettingProductOptionDTO(
                        "dog",
                        getImage(IMAGE_URL + DOG)
                ), new BettingProductOptionDTO(
                        "cat",
                        getImage(IMAGE_URL + CAT)
                ))
        ));
    }

    @Bean
    @Order(101)
    public CommandLineRunner initBettingDummyData() throws IOException {
        return args -> {
            if (bettingProductRepository.findAll().isEmpty()) {
                List<BettingProductAndOptionDTO> bettingProductAndOptionDTOS = createBettingProductAndOptionDTO();
                for (BettingProductAndOptionDTO bettingProductAndOptionDTO : bettingProductAndOptionDTOS) {
                    productCommandService.save(bettingProductAndOptionDTO);
                }
            }
        };
    }

    private MultipartFile getImage(String str) throws IOException {
        Resource resource = resourceLoader.getResource(str);
        File file = resource.getFile();
        return new CustomMultipartFile(file);
    }
}

class CustomMultipartFile implements MultipartFile {
    private final File file;

    public CustomMultipartFile(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        return "image/jpeg"; // 이미지 유형에 맞게 수정
    }

    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.copy(file.toPath(), dest.toPath());
    }
}