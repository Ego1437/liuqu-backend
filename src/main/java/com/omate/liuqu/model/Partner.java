package com.omate.liuqu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Partners")
public class Partner {
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
    @JsonIgnore
    private String password;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "partner_avatar", length = 100)
    private String partnerAvatar;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "postcode")
    private Integer postcode;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "ticket_total")
    private Integer ticketTotal = 0; // 订单总量

    @Column(name = "fans_count")
    private Integer fansCount = 0; // 粉丝数

    public PartnerStaff getPartnerStaff() {
        return partnerStaff;
    }

    // 假设staff_id关联到Customer_staff表的staff_id
    @ManyToOne
    @JoinColumn(name = "partner_staff_id", referencedColumnName = "partner_staff_id")
    private PartnerStaff partnerStaff;

    // 如果activityImage是一个JSON数组或其他复杂结构，需要适当处理
    @Column(columnDefinition = "TEXT")
    private String partnerAlbum;

    @Column(name = "review_score")
    private BigDecimal reviewScore = new BigDecimal(0);

    @ManyToMany(mappedBy = "followedPartners")
    @JsonIgnore
    private Set<User> followedByUsers = new HashSet<>();

    // Getters and setters for all fields
    public int getFollowersCount() {
        return followedByUsers.size();
    }

    public String getPartnerAvatar() {
        return partnerAvatar;
    }

    public void setPartnerAvatar(String partnerAvatar) {
        this.partnerAvatar = partnerAvatar;
    }

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

    public Integer getTicketTotal() {
        return ticketTotal;
    }

    public void setTicketTotal(Integer orderTotal) {
        this.ticketTotal = orderTotal;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public void setPartnerStaff(PartnerStaff partnerStaff) {
        this.partnerStaff = partnerStaff;
    }

    public String getPartnerAlbum() {
        return partnerAlbum;
    }

    public void setPartnerAlbum(String partnerAlbum) {
        this.partnerAlbum = partnerAlbum;
    }

    public BigDecimal getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(BigDecimal reviewScore) {
        this.reviewScore = reviewScore;
    }

}
