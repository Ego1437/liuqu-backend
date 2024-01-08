package com.omate.liuqu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer_staffs")
public class CustomerStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_staff_id", nullable = false)
    private Integer customerStaffId;

    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false, referencedColumnName = "partner_id")
    private Partner partner;

    @Column(name = "staff_name", nullable = false, length = 50)
    private String staffName;

    @Column(name = "staff_tel", length = 20)
    private String staffTelephone;

    @Column(name = "staff_email", length = 30)
    private String staffEmail;

    @Column(name = "password", length = 20)
    private String password;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 10)
    private String gender;

    // Getters and setters for all fields
}
