package com.omate.liuqu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Partner_staffs")
public class PartnerStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_staff_id", nullable = false)
    private Integer partnerStaffId;

//    @Column(nullable = false)
//    private Integer partnerId; // 假设这是另一张表的外键

    @Column(length = 50, nullable = false)
    private String staffName;

    @Column(nullable = false)
    private Integer staffType;

    @Column(length = 20, nullable = false)
    private String staffTel;

    @Column(length = 30, nullable = false)
    private String staffEmail;

    @Column(length = 20)
    private String password;

    private Integer age;

    @Column(length = 10)
    private String gender;


    // getter和setter方法
}

