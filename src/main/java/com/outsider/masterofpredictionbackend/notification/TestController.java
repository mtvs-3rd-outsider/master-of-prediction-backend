package com.outsider.masterofpredictionbackend.notification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/trigger-error")
    public String triggerError() {
        throw new RuntimeException("Test exception for Discord notification");
    }
}
