package com.outsider.masterofpredictionbackend.user.query.usersearch;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class UserQueryController {

    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/displayName")
    public List<UserSearchModel> searchByDisplayName(@RequestParam String q) {
        return userQueryService.searchByDisplayName(q);
    }

    @GetMapping("/userName")
    public List<UserSearchModel> searchByUserName(@RequestParam String q) {
        return userQueryService.searchByUserName(q);
    }
}
