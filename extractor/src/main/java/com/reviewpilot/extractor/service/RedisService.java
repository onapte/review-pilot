package com.reviewpilot.extractor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private static final String REDIS_QUEUE_KEY = "pull_request_queue";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String popOldestPayload() {
        return redisTemplate.opsForList().rightPop(REDIS_QUEUE_KEY);
    }
}
