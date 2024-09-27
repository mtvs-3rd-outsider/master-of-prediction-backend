package com.outsider.masterofpredictionbackend.betting.query.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingDetailDTO;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingViewDTO;
import com.outsider.masterofpredictionbackend.betting.query.repository.BettingImageQueryRepository;
import com.outsider.masterofpredictionbackend.betting.query.repository.BettingOptionQueryRepository;
import com.outsider.masterofpredictionbackend.betting.query.repository.BettingQueryRepository;
import com.outsider.masterofpredictionbackend.betting.query.repository.UserQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BettingProductQueryService {
    private final BettingQueryRepository bettingQueryRepository;
    private final BettingImageQueryRepository bettingImageQueryRepository;
    private final BettingOptionQueryRepository bettingOptionQueryRepository;
    private final UserQueryRepository userQueryRepository;

    public BettingProductQueryService(BettingQueryRepository bettingQueryRepository, BettingImageQueryRepository bettingImageQueryRepository, BettingOptionQueryRepository bettingOptionQueryRepository, UserQueryRepository userQueryRepository) {
        this.bettingQueryRepository = bettingQueryRepository;
        this.bettingImageQueryRepository = bettingImageQueryRepository;
        this.bettingOptionQueryRepository = bettingOptionQueryRepository;
        this.userQueryRepository = userQueryRepository;
    }

    /*
     * 1. 배팅 10개와 유저를 join 하여 조회
     * 2. 조회한 배팅에서 배팅 아이디를 추출하여 이미지 조회 전송
     * 3. 조회된 이미지를 반환할 dto 에 추가하여 반환
     * NOTE: Mongo DB 적용 검토 필요
     */
    public List<BettingViewDTO> all() {
        // NOTE: 임시값
        int limit = 10;
        int offset = 0;
        List<BettingViewDTO> bettingViewDTOS = bettingQueryRepository.findBettingAllLimit(limit, offset);
        Map<Long, BettingViewDTO> maps = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (BettingViewDTO dto : bettingViewDTOS) {
            ids.add(dto.getBettingId());
            maps.put(dto.getBettingId(), dto);
        }
        List<BettingProductImage> bettingProductImages = bettingImageQueryRepository.findAllByIds(ids);
        for (BettingProductImage item : bettingProductImages) {
            BettingViewDTO dto = maps.get(item.getBettingId());
            dto.addImgUrl(item.getImgUrl());
        }
        return bettingViewDTOS;
    }
    public Page<BettingViewDTO> allByUserId(Long userId, Pageable pageable) {
        Page<BettingViewDTO> bettingViewDTOS = bettingQueryRepository.findBettingByUserId(userId, pageable);

        Map<Long, BettingViewDTO> maps = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (BettingViewDTO dto : bettingViewDTOS) {
            ids.add(dto.getBettingId());
            maps.put(dto.getBettingId(), dto);
        }

        List<BettingProductImage> bettingProductImages = bettingImageQueryRepository.findAllByIds(ids);
        for (BettingProductImage item : bettingProductImages) {
            BettingViewDTO dto = maps.get(item.getBettingId());
            dto.addImgUrl(item.getImgUrl());
        }

        return bettingViewDTOS;
    }

    public List<BettingViewDTO> allByUserId(Long userId) {
        // NOTE: 임시값
        int limit = 10;
        int offset = 0;
        List<BettingViewDTO> bettingViewDTOS = bettingQueryRepository.findBettingByUserIdLimit(userId, limit, offset);
        Map<Long, BettingViewDTO> maps = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (BettingViewDTO dto : bettingViewDTOS) {
            ids.add(dto.getBettingId());
            maps.put(dto.getBettingId(), dto);
        }
        List<BettingProductImage> bettingProductImages = bettingImageQueryRepository.findAllByIds(ids);
        for (BettingProductImage item : bettingProductImages) {
            BettingViewDTO dto = maps.get(item.getBettingId());
            dto.addImgUrl(item.getImgUrl());
        }
        return bettingViewDTOS;
    }

    public BettingDetailDTO detail(Long id) {
        BettingDetailDTO bettingDetailDTO = bettingQueryRepository.findBettingById(id);
        bettingDetailDTO.setProductImages(bettingImageQueryRepository.findByBettingId(id));
        bettingDetailDTO.setOptions(bettingOptionQueryRepository.findByBettingId(id));
        return bettingDetailDTO;

    }

    public BigDecimal findUserPoint(Long id) {
        return userQueryRepository.findById(id).get().getPoints();
    }

}