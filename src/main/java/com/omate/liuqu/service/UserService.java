package com.omate.liuqu.service;

import com.omate.liuqu.dto.UserDTO;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
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

    public UserDTO updateUser(Long userId, UserDTO updateDTO) {
        // 从数据库中获取用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 更新用户信息
        // 这里需要根据实际的DTO内容来更新，以下仅为示例
        if (updateDTO.getUserName() != null) {
            user.setUserName(updateDTO.getUserName());
        }
        if (updateDTO.getUserEmail() != null) {
            user.setUserEmail(updateDTO.getUserEmail());
        }
        if (updateDTO.getIntroduction() != null) {
            user.setIntroduction(updateDTO.getIntroduction());
        }
        if (updateDTO.getBirthday() != null) {
            user.setBirthday(updateDTO.getBirthday());
        }
        if (updateDTO.getGender() != null) {
            user.setGender(updateDTO.getGender());
        }
        if (updateDTO.getAvatarPath() != null) {
            user.setAvatar(updateDTO.getAvatarPath());
        }
        if (updateDTO.getAddress() != null) {
            user.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getPostCode() != null) {
            user.setPostcode(updateDTO.getPostCode());
        }
        if (updateDTO.getIsSubscribe() != null) {
            user.setIsSubscribe(updateDTO.getIsSubscribe());
        }

        // 保存更新后的用户
        userRepository.save(user);

        // 将更新后的用户转换为DTO并返回
        return convertToDto(user);
    }


    public boolean changePassword(PasswordChangeRequest request) {
        // 验证验证码
        if (verificationService.verifyCode(request.getPhoneNumber(), request.getVerificationCode())) {
            // 查找用户
            User user = userRepository.findByUserTel(request.getPhoneNumber());
            if (user != null) {
                // 更新密码
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                return true;
            } else {
                throw new UsernameNotFoundException("User not found with phone number: " + request.getPhoneNumber());
            }
        } else {
            throw new IllegalArgumentException("Invalid verification code");
        }
    }

    public Set<Activity> getFavoriteActivities(Long userId) {
        return userRepository.findFavoriteActivitiesByUserId(userId);
    }

    public Set<Partner> getFollowedPartners(Long userId) {
        return userRepository.findFollowedPartnersByUserId(userId);
    }

    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            // 处理用户不存在的情况，根据你的业务需求来决定是抛出异常还是返回 null
            throw new RuntimeException("User not found with id: " + userId);
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

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserTel(user.getUserTel());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setGender(user.getGender());
        userDTO.setAvatarPath(user.getAvatar());
        userDTO.setAddress(user.getAddress());
        userDTO.setPostCode(user.getPostcode());
        userDTO.setIsSubscribe(user.getIsSubscribe());
        return userDTO;
    }

    public boolean deleteUser(String phoneNumber, String verificationCode) {
        // 验证验证码
        boolean isValid = verificationService.verifyCode(phoneNumber, verificationCode);
        if (!isValid) {
            // 验证码不正确，可以抛出一个异常或返回错误信息
            throw new IllegalArgumentException("Invalid verification code");
        }

        // 根据手机号查找用户
        User user = userRepository.findByUserTel(phoneNumber);
        if (user == null) {
            // 用户不存在，可以抛出一个异常或返回错误信息
            throw new EntityNotFoundException("User not found");
        }

        // 执行删除用户操作
        userRepository.delete(user);

        // 可以添加其他清理工作，比如删除用户相关的其他数据（如果有的话）

        return true;
    }

}
