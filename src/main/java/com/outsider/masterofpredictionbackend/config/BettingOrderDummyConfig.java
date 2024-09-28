package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProduct;
import com.outsider.masterofpredictionbackend.betting.command.domain.repository.BettingProductRepository;
import com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request.BettingOrderDTO;
import com.outsider.masterofpredictionbackend.bettingorder.command.application.service.BettingOrderCommandService;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.aggregate.BettingOrder;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.repository.BettingOrderRepository;
import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.BettingOrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.List;

@Profile("dev")
@Configuration
public class BettingOrderDummyConfig {

    private final BettingOrderRepository bettingOrderRepository;
    private final BettingProductRepository productRepository;

    public BettingOrderDummyConfig(BettingOrderRepository bettingOrderRepository, BettingProductRepository productRepository) {
        this.bettingOrderRepository = bettingOrderRepository;
        this.productRepository = productRepository;
    }


    @Bean
    @DependsOn("initBettingDummyData")
    public CommandLineRunner initBettingOrderDummyData(ApplicationContext context) {
        return args -> {
            boolean test = context instanceof ConfigurableApplicationContext && ((ConfigurableApplicationContext) context).isActive();
            if (test) {
                List<BettingProduct> bettingProducts = productRepository.findAll();
                System.out.println("??? : " + bettingProducts);
                if (!bettingProducts.isEmpty()) {
                    Long bettingId = bettingProducts.getFirst().getId();
                    bettingOrderRepository.saveAll(Arrays.asList(
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now()),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now()),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(5)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(5)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(6)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(6)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(8)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(8)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(10)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(10)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(11)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(11)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(14)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(14)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(15)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(15)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(16)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(16)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(20)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(20)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(22)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(22)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(29)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(29)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(35)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(35)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(40)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(40)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(10)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(10)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(94)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(94)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(69)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(69)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(100)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(100)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now(), LocalTime.now().plusMinutes(120)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now(), LocalTime.now().plusMinutes(120)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 1L, LocalDate.now().plusDays(1), LocalTime.now().plusMinutes(130)),
                                    new BettingOrder(1L, bettingId, BigDecimal.valueOf(100), 2L, LocalDate.now().plusDays(1), LocalTime.now().plusMinutes(130))
                            )
                    );
                }
            }
        };
    }
}
