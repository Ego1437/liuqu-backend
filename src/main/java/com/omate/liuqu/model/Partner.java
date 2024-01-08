package com.omate.liuqu.model;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.Date;

import java.util.List;
import java.util.Date;

@Entity
@Table(name = "Partners")
public class Partner {


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessTelephone() {
        return businessTelephone;
    }

    public void setBusinessTelephone(String businessTelephone) {
        this.businessTelephone = businessTelephone;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    @Column(name = "b_name", nullable = false, length = 50)
    private String businessName;

    @Column(name = "b_tel", nullable = false, length = 20)
    private String businessTelephone;

    @Column(name = "b_email", nullable = false, length = 30)
    private String businessEmail;

    @Column(name = "password", length = 20)
    private String password;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "postcode")
    private Integer postcode;

    @Column(name = "description", length = 200)
    private String description;

    public PartnerStaff getPartnerStaff() {
        return partnerStaff;
    }

    public void setPartnerStaff(PartnerStaff partnerStaff) {
        this.partnerStaff = partnerStaff;
    }

    // 假设staff_id关联到Customer_staff表的staff_id
    @ManyToOne
    @JoinColumn(name = "partner_staff_id", referencedColumnName = "partner_staff_id")
    private PartnerStaff partnerStaff;
    // Getters and setters for all fields
}
