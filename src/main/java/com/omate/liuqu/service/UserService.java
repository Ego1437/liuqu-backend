package com.omate.liuqu.service;

import com.omate.liuqu.model.LoginResponse;
import com.omate.liuqu.model.User;
import com.omate.liuqu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponse loginUser(String phoneNumber, String password) {
        // 根据手机号查找用户
        User user = userRepository.findByUserTel(phoneNumber);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            String accessToken = "1";
            String refreshToken = "";
            return new LoginResponse(accessToken, refreshToken);
        }

        // 生成JWT访问令牌和刷新令牌
        String accessToken = tokenService.createAccessToken(user);
        String refreshToken = tokenService.createRefreshToken(user);

        // 存储刷新令牌
        tokenService.storeRefreshToken(refreshToken, user.getUserId());

        // 返回令牌
        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse registerUser(String phoneNumber, String password, String verificationCode) {
        // 验证验证码
        boolean isValid = verificationService.verifyCode(phoneNumber, verificationCode);
        if (isValid) {
            // 根据手机号查找用户
            User user = userRepository.findByUserTel(phoneNumber);
            if (user != null) {
                String accessToken = "User has been registered";
                String refreshToken = "";
                // 返回注册响应，包括令牌（根据您的业务需求）
                return new LoginResponse(accessToken, refreshToken);
            }
            // 创建用户实体
            User newUser = new User();
            newUser.setUserTel(phoneNumber);
            newUser.setPassword(passwordEncoder.encode(password)); // 密码加密
            newUser.setUserName("Omate#" + generateRandomDigits(7));

            // 保存用户信息到数据库
            userRepository.save(newUser);

            // 使用TokenService生成激活令牌和待激活令牌
            String accessToken = tokenService.createAccessToken(newUser);
            String refreshToken = tokenService.createRefreshToken(newUser);

            // 存储刷新令牌
            tokenService.storeRefreshToken(refreshToken, newUser.getUserId());
            // 返回注册响应，包括令牌（根据您的业务需求）
            return new LoginResponse(accessToken, refreshToken);
        } else {
            // 可以抛出一个异常或返回错误信息
            throw new IllegalArgumentException("Invalid verification code");
        }
    }

//    public boolean changePassword(PasswordChangeRequest request) {
//        // 验证验证码
//        String correctCode = redisTemplate.opsForValue().get("verification_code:" + request.getPhoneNumber());
//        if (correctCode != null && correctCode.equals(request.getVerificationCode())) {
//            // 查找用户
//            User user = userRepository.findByUserTel(request.getPhoneNumber());
//            if (user != null) {
//                // 更新密码
//                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//                userRepository.save(user);
//                return true;
//            } else {
//                throw new UsernameNotFoundException("User not found with phone number: " + request.getPhoneNumber());
//            }
//        } else {
//            throw new InvalidVerificationCodeException("Invalid verification code");
//        }
//    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
