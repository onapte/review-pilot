package com.reviewpilot.extractor.controller;

import com.reviewpilot.extractor.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PayloadController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/next-payload")
    public String getNextPayload() {
        String payload = redisService.popOldestPayload();
        if (payload == null) {
            return "{\"message\": \"No payloads in queue.\"}";
        }
        return payload;
    }

    @GetMapping("/healthz")
    public String healthCheck() {
        return "Status: OK";
    }
}
