package com.omate.liuqu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisTestService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String testConnection() {
        // 设置一个键值对
        stringRedisTemplate.opsForValue().set("testKey", "Hello, Redis!");

        // 获取并返回设置的值
        return stringRedisTemplate.opsForValue().get("testKey");
    }
}
