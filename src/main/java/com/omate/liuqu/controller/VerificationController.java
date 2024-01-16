package com.omate.liuqu.controller;

import com.omate.liuqu.model.Result;
import com.omate.liuqu.model.User;
import com.omate.liuqu.service.TokenService;
import com.omate.liuqu.service.UserService;
import com.omate.liuqu.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/sendCode")
    public ResponseEntity<Result> sendVerificationCode(@RequestParam String phoneNumber) {
        verificationService.generateAndSendCode(phoneNumber);
        Result result = new Result();
        result.setResultSuccess(0, "Verification code sent"); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Result> refreshAccessToken(@RequestParam("refreshToken") String refreshToken, @RequestParam("userId") Long userId) {

        // 验证 refreshToken
        boolean isValid = tokenService.validateRefreshToken(refreshToken, userId);
        if (!isValid) {
            throw new RuntimeException("Invalid refresh token");
        }

        // 获取用户信息
        User user = userService.getUserById(userId);

        Result result = new Result();
        result.setResultSuccess(0, tokenService.createAccessToken(user)); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

}
