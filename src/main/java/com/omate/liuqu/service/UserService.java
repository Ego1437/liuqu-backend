package com.omate.liuqu.service;

import com.omate.liuqu.model.User;
import com.omate.liuqu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser(String phoneNumber, String password, String verificationCode) {
        // 验证验证码
        String correctCode = redisTemplate.opsForValue().get("verification_code:" + phoneNumber);
        if (correctCode != null && correctCode.equals(verificationCode)) {
            // 创建用户实体
            User newUser = new User();
            newUser.setUserTel(phoneNumber);
            newUser.setPassword(passwordEncoder.encode(password)); // 密码加密
            newUser.setUserName("User" + generateRandomDigits(7));

            // 保存用户信息到数据库
            userRepository.save(newUser);

            // 生成激活令牌并存储到 Redis
            String activationToken = generateRandomDigits(20); // 这应该是一个更安全的令牌生成方法
            redisTemplate.opsForValue().set("activation_token:" + newUser.getUserName(), activationToken, 3, TimeUnit.HOURS);

            // 发送激活令牌或者在响应中返回（根据您的业务逻辑）
            // 在3小时后，设置一个15天的待激活令牌
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set("pending_activation_token:" + newUser.getUserName(), generateRandomDigits(20), 15, TimeUnit.DAYS);

            return true;
        } else {
            return false;
        }
    }



    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
