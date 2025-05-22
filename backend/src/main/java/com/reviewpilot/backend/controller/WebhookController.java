package com.reviewpilot.backend.controller;

import com.reviewpilot.backend.service.WebhookQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebhookController {
    @Autowired
    private WebhookQueueService queueService;

    @PostMapping("/webhook")
    public String receiveWebhook(@RequestBody String payload) {
        System.out.println("Received GitHub Webhook!\n" + payload);
        queueService.sendToQueue(payload);
        return "Webhook received";
    }

    @GetMapping("/healthz")
    public String healthCheck() {
        return "OK";
    }
}
