package com.outsider.masterofpredictionbackend.betting.command.application.service;

import com.outsider.masterofpredictionbackend.bettingorder.command.application.dto.request.BettingOrderDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BettingEventService {
    // 상품별 룸을 관리하기 위한 Map (상품 ID -> 연결된 SSE Emitters)
    private final Map<Long, List<SseEmitter>> productRooms = new ConcurrentHashMap<>();

    /**
     * 클라이언트가 특정 상품(room)에 연결
     */
    public SseEmitter connectToProductRoom(Long productId) {
        SseEmitter emitter = new SseEmitter();
        productRooms.computeIfAbsent(productId, k -> new ArrayList<>()).add(emitter);

        // 연결 종료 및 오류 처리
        emitter.onCompletion(() -> removeEmitter(productId, emitter));
        emitter.onTimeout(() -> removeEmitter(productId, emitter));
        emitter.onError((e) -> removeEmitter(productId, emitter));

        return emitter;
    }

    /**
     * 특정 상품(room)에 이벤트 전달
     */
    public void notifyProductRoom(BettingOrderDTO message) {
        List<SseEmitter> emitters = productRooms.get(message.getBettingId());
        if (emitters != null) {
            List<SseEmitter> deadEmitters = new ArrayList<>();
            for (SseEmitter emitter : emitters) {
                try {
                    // 룸 내 사용자들에게 이벤트 전송
                    emitter.send(message);
                } catch (IOException e) {
                    deadEmitters.add(emitter);
                }
            }
            emitters.removeAll(deadEmitters); // 비정상 연결 제거
        }
    }

    /**
     * 특정 Emitter를 룸에서 제거
     */
    private void removeEmitter(Long productId, SseEmitter emitter) {
        List<SseEmitter> emitters = productRooms.get(productId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                productRooms.remove(productId);
            }
        }
    }
}
