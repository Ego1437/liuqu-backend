package com.omate.liuqu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Partner partner;

    @Column(name = "partner_id", updatable = false, insertable = false)
    private Long partnerId;

    @Column(name = "staff_name", nullable = false, length = 50)
    private String staffName;

    @Column(name = "staff_tel", length = 20)
    private String staffTelephone;

    @Column(name = "staff_email", length = 30)
    private String staffEmail;

    @Column(name = "password", length = 20)
    @JsonIgnore
    private String password;

    @Column(name = "age")
    @JsonIgnore
    private Integer age;

    @Column(name = "gender", length = 10)
    @JsonIgnore
    private String gender;

    // Getters and setters for all fields
    public Integer getCustomerStaffId() {
        return customerStaffId;
    }

    public void setCustomerStaffId(Integer customerStaffId) {
        this.customerStaffId = customerStaffId;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffTelephone() {
        return staffTelephone;
    }

    public void setStaffTelephone(String staffTelephone) {
        this.staffTelephone = staffTelephone;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
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

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

}
