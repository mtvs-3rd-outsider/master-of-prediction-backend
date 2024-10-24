package com.outsider.masterofpredictionbackend.bettingorder.command.domain.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductOption;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.BettingProductService;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.naver.ApiBettingProductService;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.naver.ApiNaverResponse;
import com.outsider.masterofpredictionbackend.betting.command.domain.service.naver.GameNaver;
import com.outsider.masterofpredictionbackend.bettingorder.query.repository.BettingOptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class NaverBettingScheduledTasks {

    private final BettingProductRepository bettingProductRepository;
    private final BettingOptionRepository bettingOptionRepository;
    private final ApiBettingProductService apiBettingProductService;
    private final BettingProductService bettingProductService;

    public NaverBettingScheduledTasks(BettingProductRepository bettingProductRepository, BettingOptionRepository bettingOptionRepository, ApiBettingProductService apiBettingProductService, BettingProductService bettingProductService) {
        this.bettingProductRepository = bettingProductRepository;
        this.bettingOptionRepository = bettingOptionRepository;
        this.apiBettingProductService = apiBettingProductService;
        this.bettingProductService = bettingProductService;
    }


    // 매일 00:00에 실행 (하루에 한번)
    @Scheduled(cron = "0 0 0 * * ?")
    public void runRegisterFootballTask() {
        // TODO: 관리자 계정 아이디를 넣어야 함
        apiBettingProductService.apiKFootball(100L);

    }

    // 매일 00:00에 실행 (하루에 한번)
    @Scheduled(cron = "0 0 0 * * ?")
    public void runSettlementFootballTask() {
        List<BettingProduct> bettingProductLists = bettingProductRepository.findByScheduledBettingProduct();
        List<BettingProduct> saveBuffer = new ArrayList<>();

        ApiNaverResponse.Result apiResult = null;
        LocalDate currentDate = null;


        for (BettingProduct bettingProduct : bettingProductLists) {
            if (shouldFetchApi(currentDate, bettingProduct.getDeadlineDate())) {
                ApiNaverResponse apiNaverResponse = apiBettingProductService.sendKFootballApi(bettingProduct.getDeadlineDate());
                currentDate = bettingProduct.getDeadlineDate();
                apiResult = apiNaverResponse.getResult();
            }
            handleGameResults(bettingProduct, apiResult, saveBuffer);
        }
        if (!saveBuffer.isEmpty()){
            log.info("saveBuffer: {}", saveBuffer);
            bettingProductRepository.saveAll(saveBuffer);
        }

    }
    private void handleWinningOption(BettingProduct bettingProduct, String winner, List<BettingProductOption> options, List<BettingProduct> saveBuffer) {
        Long winningOptionId = null;

        if ("AWAY".equals(winner)) {
            // 0번째 인덱스가 away
            winningOptionId = options.getFirst().getId();
        } else if ("HOME".equals(winner)) {
            // 1번째 인덱스가 home
            winningOptionId = options.get(1).getId();
        } else{
        //     환불
        }

        if (winningOptionId != null) {
            bettingProduct.setWinningOption(winningOptionId);
            // TODO: 관리자 계정 아이디를 넣어야 함
            bettingProductService.settlementBettingProduct(bettingProduct.getId(), 100L, winningOptionId);
            saveBuffer.add(bettingProduct);
        }
    }

    private boolean shouldFetchApi(LocalDate currentDate, LocalDate deadlineDate) {
        return currentDate == null || !currentDate.isEqual(deadlineDate);
    }

    private void handleGameResults(BettingProduct bettingProduct, ApiNaverResponse.Result result, List<BettingProduct> saveBuffer) {
        for (GameNaver game : result.getGames()) {
            if (game.getGameId().equals(bettingProduct.getApiGameId())) {
                if ("경기종료".equals(game.getStatusInfo())) {
                    List<BettingProductOption> options = bettingOptionRepository.findByBettingId(bettingProduct.getId());

                    if (!game.getWinner().isBlank()) {
                        log.info("game.getWinner(): {}", game.getWinner());
                        handleWinningOption(bettingProduct, game.getWinner(), options, saveBuffer);
                    }
                }
                break;
            }
        }
    }
}
