package com.omate.liuqu.model;

public class PasswordChangeRequest {

    private String phoneNumber;
    private String verificationCode;
    private String newPassword;

    // 构造函数、getter和setter略

    // 构造函数
    public PasswordChangeRequest() {
        // 默认构造函数
    }

    // Getters 和 Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
