package com.outsider.masterofpredictionbackend.betting.command.domain.service.naver;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.*;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductImageRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductOptionRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ApiBettingProductService {

    private final WebClient webClient;
    private final BettingProductRepository bettingProductRepository;
    private final BettingProductImageRepository bettingProductImageRepository;
    private final BettingProductOptionRepository bettingProductOptionRepository;
    private final APIOptionManagement apiOptionManagement;

    // WebClient 빌더를 주입받아 설정
    public ApiBettingProductService(WebClient.Builder webClientBuilder, BettingProductRepository bettingProductRepository, BettingProductImageRepository bettingProductImageRepository, BettingProductOptionRepository bettingProductOptionRepository, APIOptionManagement apiOptionManagement) {
        this.webClient = webClientBuilder.build(); // 기본 URL 설정
        this.bettingProductRepository = bettingProductRepository;
        this.bettingProductImageRepository = bettingProductImageRepository;
        this.bettingProductOptionRepository = bettingProductOptionRepository;
        this.apiOptionManagement = apiOptionManagement;
    }

    public ApiNaverResponse sendApi(LocalDate localDate, APIBettingProductCategory category) {
        URI url = apiOptionManagement.getApi(category, localDate);
        log.info("api url: {}", url);
        return webClient.get()
                .uri(apiOptionManagement.getApi(category, localDate))
                .retrieve()
                .bodyToMono(ApiNaverResponse.class).block();
    }

    /**
     * 하루 뒤의 경기를 등록한다
     * 배팅 제목: awayTeamName vs homeTeamName
     * 배팅 내용: awayTeamName vs homeTeamName 승자는?
     * 배팅 마감시간: gameDateTime (yyyy-MM-ddTHH:mm:ss)
     * 배팅 옵션
     * 1.
     * 팀 이름: awayTeamName
     * 팀 사진: awayTeamEmblemUrl
     * 2.
     * 팀 이름: homeTeamName
     * 팀 사진: homeTeamEmblemUrl
     * 3.
     * 배팅 메인 이미지는 1과 2의 팀 사진으로 대체한다
     */
    @Transactional
    public void saveApiConvertBettingProduct(ApiNaverResponse.Result result, Long userId, APIBettingProductCategory category) {
        List<BettingProduct> bettingProductsContainer = new ArrayList<>();
        List<List<BettingOptionContainer>> bettingOptionContainer = new ArrayList<>();

        result.getGames().stream()
                .filter(game -> !game.getHomeTeamName().isBlank() && !game.getAwayTeamName().isBlank())
                .forEach(gameNaver -> {
                    BettingProduct bettingProduct = createBettingProduct(gameNaver, userId, category);
                    List<BettingOptionContainer> bettingProductOptions = createBettingOptionContainers(gameNaver);

                    bettingProductsContainer.add(bettingProduct);
                    bettingOptionContainer.add(bettingProductOptions);
                });

        bettingProductRepository.saveAll(bettingProductsContainer);
        int bettingProductsContainerSize = bettingProductsContainer.size();
        List<BettingProductOption> bettingProductOptionsContainer = new ArrayList<>();
        List<BettingProductImage> bettingProductImagesContainer = new ArrayList<>();
        for (int i = 0; i < bettingProductsContainerSize; i++) {
            BettingProduct bettingProduct = bettingProductsContainer.get(i);

            List<BettingOptionContainer> bettingProductOptions = bettingOptionContainer.get(i);

            bettingProductOptions.forEach(option -> {
                bettingProductOptionsContainer.add(new BettingProductOption(bettingProduct.getId(), option.content, option.imgUrl));
                bettingProductImagesContainer.add(new BettingProductImage(bettingProduct.getId(), option.imgUrl));
            });
        }
        bettingProductOptionRepository.saveAll(bettingProductOptionsContainer);
        bettingProductImageRepository.saveAll(bettingProductImagesContainer);
    }

    @Transactional
    public void apiTotal(Long userId, LocalDate localDate) {
        // api 받아오기
        for (APIBettingProductCategory category : APIBettingProductCategory.values()) {
            ApiNaverResponse response = sendApi(localDate, category);
            log.info("api response: {}", response);
            ApiNaverResponse.Result result = Objects.requireNonNull(response).getResult();
            saveApiConvertBettingProduct(result, userId, category);
        }
    }

    @Transactional
    public ApiNaverResponse apiKFootball(Long userId, LocalDate localDate) {
        // api 받아오기
        ApiNaverResponse response = sendApi(localDate, APIBettingProductCategory.KOREA_FOOTBALL);
        log.info("api response: {}", response);
        ApiNaverResponse.Result result = Objects.requireNonNull(response).getResult();
        saveApiConvertBettingProduct(result, userId, APIBettingProductCategory.KOREA_FOOTBALL);

        return response;
    }

    @Transactional
    public ApiNaverResponse apiBasketball(Long userId, LocalDate localDate) {
        // api 받아오기
        ApiNaverResponse response = sendApi(localDate, APIBettingProductCategory.BASKETBALL);
        log.info("api response: {}", response);
        ApiNaverResponse.Result result = Objects.requireNonNull(response).getResult();
        saveApiConvertBettingProduct(result, userId, APIBettingProductCategory.BASKETBALL);

        return response;
    }

    private BettingProduct createBettingProduct(GameNaver gameNaver, Long userId, APIBettingProductCategory category) {
        LocalDateTime gameDateTime = LocalDateTime.parse(gameNaver.getGameDateTime());
        String matchTitle = gameNaver.getAwayTeamName() + " vs " + gameNaver.getHomeTeamName();
        StringBuilder content = new StringBuilder();
        content.append(gameDateTime.toLocalDate())
                .append(" ")
                .append(gameDateTime.toLocalTime())
                .append(" ")
                .append(gameNaver.getAwayTeamName())
                .append(" vs ")
                .append(gameNaver.getHomeTeamName())
                .append(" 승자는?");

        BettingProduct bettingProduct = new BettingProduct(
                matchTitle,
                content.toString(),
                userId,
                1,
                gameDateTime.toLocalDate(),
                gameDateTime.toLocalTime(),
                false,
                null
        );
        bettingProduct.setType(BettingProductType.NAVER);
        bettingProduct.setApiGameId(gameNaver.getGameId());
        bettingProduct.setApiBettingProductCategory(category);

        return bettingProduct;
    }

    private List<BettingOptionContainer> createBettingOptionContainers(GameNaver gameNaver) {
        return List.of(
                new BettingOptionContainer(gameNaver.getAwayTeamName(), gameNaver.getAwayTeamEmblemUrl()),
                new BettingOptionContainer(gameNaver.getHomeTeamName(), gameNaver.getHomeTeamEmblemUrl())
        );
    }

    private record BettingOptionContainer(String content, String imgUrl) {
    }
}

