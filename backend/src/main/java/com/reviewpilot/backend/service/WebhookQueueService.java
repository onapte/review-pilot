package com.reviewpilot.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebhookQueueService {

    private static final String REDIS_QUEUE_KEY = "pull_request_queue";

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public WebhookQueueService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sendToQueue(String payload) {
        redisTemplate.opsForList().leftPush(REDIS_QUEUE_KEY, payload);
    }
}
