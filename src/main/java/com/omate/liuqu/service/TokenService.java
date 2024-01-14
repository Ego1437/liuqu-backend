package com.omate.liuqu.service;

import com.omate.liuqu.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String secretKey = "B981AD8E3ED3EC7955C1B599D6AF8"; // 使用您的密钥

    public String createAccessToken(User user) {
        long validityInMilliseconds = TimeUnit.HOURS.toMillis(3); // 3小时
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(User user) {
        long validityInMilliseconds = TimeUnit.DAYS.toMillis(15); // 15天
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public void storeRefreshToken(String refreshToken, Long userId) {
        redisTemplate.opsForValue().set("RefreshToken:" + userId, refreshToken, 15, TimeUnit.DAYS);
    }

    public boolean validateRefreshToken(String refreshToken, Long userId) {
        String storedToken = redisTemplate.opsForValue().get("RefreshToken:" + userId);
        return refreshToken.equals(storedToken);
    }
}

