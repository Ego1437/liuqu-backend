package com.omate.liuqu.controller;

import com.omate.liuqu.service.RedisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RedisTestController {

    private final RedisTestService redisTestService;

    @Autowired
    public RedisTestController(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }

    @GetMapping("/test-redis")
    public String testRedis() {
        return redisTestService.testConnection();
    }
}
