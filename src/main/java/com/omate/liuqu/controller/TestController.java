package com.omate.liuqu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    // 使用@Value注解将application.properties中的参数注入
    @Value("${spring.redis.host}")
    private String message;

    @GetMapping("/test")
    public String test() {
        // 返回一个简单的消息，该消息来自application.properties文件
        return message;
    }
}