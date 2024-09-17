package com.outsider.masterofpredictionbackend.betting.query.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingProductImage;
import com.outsider.masterofpredictionbackend.betting.query.dto.BettingUserDTO;
import com.outsider.masterofpredictionbackend.betting.query.repository.BettingImageQueryRepository;
import com.outsider.masterofpredictionbackend.betting.query.repository.BettingQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BettingProductQueryService {
    private final BettingQueryRepository bettingQueryRepository;
    private final BettingImageQueryRepository bettingImageQueryRepository;

    public BettingProductQueryService(BettingQueryRepository bettingQueryRepository, BettingImageQueryRepository bettingImageQueryRepository) {
        this.bettingQueryRepository = bettingQueryRepository;
        this.bettingImageQueryRepository = bettingImageQueryRepository;
    }

    /*
     * 1. 배팅 10개와 유저를 join 하여 조회
     * 2. 조회한 배팅에서 배팅 아이디를 추출하여 이미지 조회 전송
     * 3. 조회된 이미지를 반환할 dto 에 추가하여 반환
     * NOTE: Mongo DB 적용 검토 필요
     */
    public List<BettingUserDTO> all(){
        // NOTE: 임시값
        int limit = 10;
        int offset = 0;
        List<BettingUserDTO> bettingUserDTOS = bettingQueryRepository.findBettinglimit(limit, offset);
        Map<Long, BettingUserDTO> maps = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for(BettingUserDTO dto : bettingUserDTOS){
            ids.add(dto.getBettingId());
            maps.put(dto.getBettingId(), dto);
        }
        List<BettingProductImage> bettingProductImages = bettingImageQueryRepository.findAllByIds(ids);
        for(BettingProductImage item: bettingProductImages){
            BettingUserDTO dto = maps.get(item.getBettingId());
            dto.addImgUrl(item.getImgUrl());
        }
        return bettingUserDTOS;
    }
}
