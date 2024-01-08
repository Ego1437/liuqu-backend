package com.omate.liuqu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "User")
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

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    private String avatarPath = "http://13.236.138.98:8082/api/view/cHJvZmlsZS1waWN0dXJl_1698819116508.jpg";
    @NotBlank(message = "UserType is mandatory")

    @Column(length = 20)
    private String password;

    private Integer age;

    @Column(length = 10)
    private String gender;

    @Column(length = 100)
    private String address;

    private Integer postcode;

    @Column(name = "is_subscribe")
    private Integer isSubscribe;

    // getter和setter方法
}
