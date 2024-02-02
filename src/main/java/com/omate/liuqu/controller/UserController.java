package com.omate.liuqu.controller;

import com.omate.liuqu.dto.*;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.*;
import com.omate.liuqu.service.*;
import com.omate.liuqu.utils.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/api") // This means URL's start with /demo (after Application path)
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserService userService;

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * *
     * updateUserInfoById
     * @param user
     * @return userDTO
     */
    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserTel(user.getUserTel());
        userDTO.setGender(user.getGender());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setAvatarPath(user.getAvatar());
        userDTO.setPostCode(user.getPostcode());
        userDTO.setAddress(user.getAddress());
        userDTO.setIsSubscribe(user.getIsSubscribe());
        return userDTO;
    }

    @PostMapping("/login")
    public ResponseEntity<Result> loginUser(@RequestParam String phoneNumber, @RequestParam String password) {
        logger.warn("Registering user with phoneNumber: {}, password: {}", phoneNumber, password);
        Result result = new Result();
        try {
            LoginResponse response = userService.loginUser(phoneNumber, password);
            if (response.getAccessToken() == "1"){
                result.setResultFailed(1); // 使用0作为成功代码，您可以根据需要更改这个值
            }else {
                result.setResultSuccess(0, response);
            }
            logger.warn("Registering user with result: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.setResultFailed(10, "Login failed: " + e.getMessage());
            logger.warn("Registering user with result: {}", result);
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Result> deleteUser(@RequestParam String phoneNumber, @RequestParam String verificationCode) {
        logger.info("Request to delete user with phoneNumber: {}", phoneNumber);
        Result result = new Result();

        try {
            boolean isDeleted = userService.deleteUser(phoneNumber, verificationCode);
            if (isDeleted) {
                result.setResultSuccess(0, "User successfully deleted");
                return ResponseEntity.ok(result);
            } else {
                result.setResultFailed(1, "User deletion failed");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IllegalArgumentException e) {
            result.setResultFailed(2, "Invalid verification code: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (EntityNotFoundException e) {
            result.setResultFailed(3, "User not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } catch (Exception e) {
            result.setResultFailed(4, "Error during deletion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

    }


    @PostMapping("/register")
    public ResponseEntity<Result> registerUser(@RequestParam String phoneNumber,
                                                      @RequestParam String password,
                                                      @RequestParam String verificationCode) {
        logger.warn("Registering user with phoneNumber: {}, password: {}, verificationCode {}", phoneNumber, password, verificationCode);
        Result result = new Result();
        try {
            LoginResponse response = userService.registerUser(phoneNumber, password, verificationCode);
            if (response.getAccessToken() == "User has been registered"){
                result.setResultFailed(2); // 使用0作为成功代码，您可以根据需要更改这个值
            }else {
                result.setResultSuccess(0, response);
            }
            logger.warn("Registering user with result: {}", result);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            result.setResultFailed(5);
            logger.warn("Registering user with result: {}", result);
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/userInfo")
    public ResponseEntity<Result> getUserInfo(@RequestHeader("Authorization") String accessToken) {
        // 移除前缀 "Bearer "
        String token = accessToken.replace("Bearer ", "");

        // 解析并验证 token
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenService.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = Long.parseLong(claims.getSubject());

        // 从数据库或其他服务中获取用户信息
        Result result = new Result();
        result.setResultSuccess(0, userService.getUserById(userId));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Result> changePassword(@RequestParam String phoneNumber,
                                                 @RequestParam String newPassword,
                                                 @RequestParam String verificationCode) {
        logger.info("Registering user with phoneNumber: {}, newPassword: {}, verificationCode {}", phoneNumber, newPassword, verificationCode);
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setNewPassword(newPassword);
        request.setPhoneNumber(phoneNumber);
        request.setVerificationCode(verificationCode);
        Result result = new Result();
        try {
            boolean isChanged = userService.changePassword(request);
            if (isChanged) {
                result.setResultSuccess(0, "Password changed successfully");
            } else {
                result.setResultFailed(10);
            }
            return ResponseEntity.ok(result);
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            result.setResultFailed(5, e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUserInfoById/{userId}")
    public ResponseEntity<Result> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO updateDTO) {
        Result result = new Result();
        try {
            UserDTO updatedUser = userService.updateUser(userId, updateDTO);
            result.setResultSuccess(0, updatedUser);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            result.setResultFailed(1, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } catch (Exception e) {
            result.setResultFailed(2, "Update failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/{userId}/favorite-activities")
    public ResponseEntity<?> getFavoriteActivities(@PathVariable Long userId) {
        Result result = new Result();
        result.setResultSuccess(0, userService.getFavoriteActivities(userId)); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}/followed-partners")
    public ResponseEntity<?> getFollowedPartners(@PathVariable Long userId) {
        Result result = new Result();
        result.setResultSuccess(0, userService.getFollowedPartners(userId)); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }
}