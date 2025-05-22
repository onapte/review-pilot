package com.reviewpilot.extractor.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueConsumerService {

    private static final String REDIS_QUEUE_KEY = "pull_request_queue";
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        System.out.println("Extractor microservice has started.");
    }

    @Scheduled(fixedDelay = 5000)
    public void pollQueue() {
        String payload = redisTemplate.opsForList().rightPop(REDIS_QUEUE_KEY);
        if (payload != null) {
            System.out.println("Processing payload...\n" + payload);
            
        }
    }
}
