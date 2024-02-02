package com.omate.liuqu.service;

import com.omate.liuqu.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    private final ConcurrentHashMap<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    @Value("${app.jwt.secretKey}")
    private String base64SecretKey;

    public SecretKey getSecretKey() {
        // 解码Base64编码的密钥
        byte[] decodedKey = Base64.getDecoder().decode(base64SecretKey);
        // 使用解码后的字节数组创建一个新的SecretKeySpec对象
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
    }


    public String createAccessToken(User user) {

        long validityInMilliseconds = TimeUnit.DAYS.toMillis(90); // 3小时
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
    }

    public String createRefreshToken(User user) {
        long validityInMilliseconds = TimeUnit.DAYS.toMillis(15); // 15天
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
    }

    public void storeRefreshToken(String refreshToken, Long userId) {
        redisTemplate.opsForValue().set("RefreshToken:" + userId, refreshToken, 15, TimeUnit.DAYS);
//        String key = "RefreshToken:" + userId;
//        refreshTokenStore.put(key, refreshToken);
//         在 15 天后移除token
//        scheduler.schedule(() -> {
//            refreshTokenStore.remove(key);
//        }, 15, TimeUnit.DAYS);
    }


    public boolean validateRefreshToken(String refreshToken, Long userId) {
        String storedToken = redisTemplate.opsForValue().get("RefreshToken:" + userId);
//        String storedToken = refreshTokenStore.get("RefreshToken:" + userId);
        return refreshToken.equals(storedToken);
    }
    @PreDestroy
    public void destroy() {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}

