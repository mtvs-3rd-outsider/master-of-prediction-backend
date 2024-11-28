package com.outsider.masterofpredictionbackend.categorychannel.query;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannelManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category-channels")
@RequiredArgsConstructor
public class CategoryChannelManagerQueryController {

    private final CategoryChannelManagerQueryService categoryChannelManagerService;



    @GetMapping("/{channelId}/management")
    public ResponseEntity<CategoryChannelManager> getManagerInfo(
            @PathVariable Long channelId,
            @RequestParam Long userId
    ) {
        CategoryChannelManager manager = categoryChannelManagerService.getManagerInfo(channelId, userId);
        return ResponseEntity.ok(manager);
    }


    @GetMapping("/{channelId}/managers")
    public ResponseEntity<List<CategoryChannelManager>> getChannelManagers(
            @PathVariable Long channelId
    ) {
        List<CategoryChannelManager> managers = categoryChannelManagerService.getChannelManagers(channelId);
        return ResponseEntity.ok(managers);
    }

    @GetMapping("/{channelId}/is-manager")
    public ResponseEntity<Boolean> isChannelManager(
            @PathVariable Long channelId,
            @RequestParam Long userId
    ) {
        boolean isManager = categoryChannelManagerService.isChannelManager(channelId, userId);
        return ResponseEntity.ok(isManager);
    }
}
