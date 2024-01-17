package com.omate.liuqu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatarPath) {
        this.avatar = avatarPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public Integer getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(Integer isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // 确保这里的列名与数据库中的列名一致
    private Long userId;

    @Column(length = 50, nullable = false)
    private String userName;

    @Column(length = 20, nullable = false)
    private String userTel;

    @Column(length = 30, nullable = false)
    private String userEmail;

    private String avatar = "http://13.236.138.98:8082/api/view/cHJvZmlsZS1waWN0dXJl_1698819116508.jpg";
    @NotBlank(message = "UserType is mandatory")

    @Column(length = 20)
    @JsonIgnore  // 阻止序列化商家信息
    private String password;

    private LocalDate birthday;

    @Column(length = 10)
    private String gender;

    @Column(length = 100)
    private String address;

    private Integer postcode;

    @Column(name = "is_subscribe")
    private Integer isSubscribe;

    @Column(length = 255)
    private String introduction;
    // getter和setter方法
}
