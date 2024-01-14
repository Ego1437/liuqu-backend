package com.omate.liuqu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsService smsService;

    public void generateAndSendCode(String phoneNumber) {
        String code = generateCode();
        storeCode(phoneNumber, code);
        smsService.sendSms(phoneNumber, "Your verification code is: " + code);
    }

    private String generateCode() {
        return String.format("%04d", new Random().nextInt(10000)); // 生成4位随机数字
    }

    private void storeCode(String phoneNumber, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(phoneNumber, code, 5, TimeUnit.MINUTES); // 存储验证码，有效期为5分钟
    }

    public boolean verifyCode(String phoneNumber, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        return storedCode != null && storedCode.equals(inputCode);
    }
}
