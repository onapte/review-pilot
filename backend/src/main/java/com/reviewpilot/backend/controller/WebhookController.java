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
    queueService.sendToQueue(payload);
    return "Webhook received";
  }

  @GetMapping("/healthz")
  public String healthCheck() {
    return "OK";
  }

  // For development purposes
  @GetMapping("/test-payload")
  public String pushRealPayload() {
    String realPayload = """
        {
          "action": "opened",
          "number": 1,
          "pull_request": {
            "url": "https://api.github.com/repos/onapte/temporal-minds/pulls/1",
            "id": 2535966356,
            "html_url": "https://github.com/onapte/temporal-minds/pull/1",
            "diff_url": "https://github.com/onapte/temporal-minds/pull/1.diff",
            "patch_url": "https://github.com/onapte/temporal-minds/pull/1.patch",
            "issue_url": "https://api.github.com/repos/onapte/temporal-minds/issues/1",
            "number": 1,
            "state": "open",
            "title": "Refactor text generation logic and modularize prompt construction",
            "user": {
              "login": "onapte",
              "id": 97520729
            },
            "body": "This PR refactors the original text generation script to improve readability, modularity, and maintainability.",
            "created_at": "2025-05-22T04:19:25Z",
            "updated_at": "2025-05-22T04:19:25Z",
            "head": {
              "ref": "alt-branch-1",
              "sha": "6897117ba649fe74321c76bb1da7d5383dbdefa8"
            },
            "base": {
              "ref": "main",
              "sha": "62f3119a1ce8b820af3ae457efd5e1203d7483f7"
            }
          },
          "repository": {
            "id": 974507334,
            "name": "temporal-minds",
            "full_name": "onapte/temporal-minds"
          },
          "sender": {
            "login": "onapte",
            "id": 97520729
          }
        }
        """;

    queueService.sendToQueue(realPayload);
    return "Payload pushed to Redis queue";
  }

}
