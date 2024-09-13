package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class UserQueryController {

    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    // displayName 검색에 페이징과 기본값 적용
    @GetMapping("/displayName")
    public Page<UserSearchModel> searchByDisplayName(
            @RequestParam String q,
            @PageableDefault(page = 0, size = 10, sort = "displayName", direction = Sort.Direction.ASC) Pageable pageable) {
        return userQueryService.searchByDisplayName(q, pageable);
    }

    // userName 검색에 페이징과 기본값 적용
    @GetMapping("/userName")
    public Page<UserSearchModel> searchByUserName(
            @RequestParam String q,
            @PageableDefault(page = 0, size = 10, sort = "userName", direction = Sort.Direction.ASC) Pageable pageable) {
        return userQueryService.searchByUserName(q, pageable);
    }
}
