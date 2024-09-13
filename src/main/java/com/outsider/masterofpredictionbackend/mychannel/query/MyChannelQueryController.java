package com.outsider.masterofpredictionbackend.mychannel.query;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class MyChannelQueryController {

    private final MyChannelQueryService myChannelQueryService;

    @Autowired
    public MyChannelQueryController(MyChannelQueryService myChannelQueryService) {
        this.myChannelQueryService = myChannelQueryService;
    }

    // 팔로워 수를 조회하는 엔드포인트
    @GetMapping("/{channelId}/follower-count")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable Long channelId) {
        int followerCount = myChannelQueryService.getFollowerCount(channelId);
        return ResponseEntity.ok(followerCount);
    }

    // 팔로잉 수를 조회하는 엔드포인트
    @GetMapping("/{channelId}/following-count")
    public ResponseEntity<Integer> getFollowingCount(@PathVariable Long channelId) {
        int followingCount = myChannelQueryService.getFollowingCount(channelId);
        return ResponseEntity.ok(followingCount);
    }
}
