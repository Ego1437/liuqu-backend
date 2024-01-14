package com.omate.liuqu.controller;

import com.omate.liuqu.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/sendCode")
    public String sendVerificationCode(@RequestParam String phoneNumber) {
        verificationService.generateAndSendCode(phoneNumber);
        return "Verification code sent";
    }

    @PostMapping("/verifyCode")
    public String verifyCode(@RequestParam String phoneNumber, @RequestParam String code) {
        boolean isValid = verificationService.verifyCode(phoneNumber, code);
        return isValid ? "Verification successful" : "Invalid verification code";
    }
}
