package com.omate.liuqu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;

    // 假设partner_id关联到Partner表的partner_id
    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "partner_id")
    @JsonIgnore  // 阻止序列化商家信息
    private Partner partner;

    @Column(name = "partner_id", updatable = false, insertable = false)
    private Long partnerId;

    // 假设staff_id关联到Customer_staff表的staff_id
    @ManyToOne
    @JoinColumn(name = "customer_staff_id", referencedColumnName = "customer_staff_id")
    private CustomerStaff customerStaff;

    private String activityAddress;

    // 如果activityImage是一个JSON数组或其他复杂结构，需要适当处理
    @Column(columnDefinition = "TEXT")
    private String activityImage;

    private String activityName;

    private Integer activityDuration;

    // 如果portfolio是一个JSON对象或数组，需要适当处理
    @Column(columnDefinition = "TEXT")
    private String portfolio;

    // 如果activityDetail是一个JSON对象或数组，需要适当处理
    @Column(columnDefinition = "TEXT")
    private String activityDetail;

    private Integer activityStatus;

    @ManyToMany
    @JoinTable(
            name = "activity_tags",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    // getters and setters
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Column(name = "category_level_1")
    private Integer categoryLevel1;

    @Column(name = "category_level_2")
    private Integer categoryLevel2;

    @OneToMany(mappedBy = "activity")
    private List<Event> events;

    private Integer verificationType;

    @Column(columnDefinition = "TEXT")
    private String collaborators;

    @Column(name = "fans_count")
    private Integer fansCount = 0; // 粉丝数

    @ManyToMany(mappedBy = "favoriteActivities")
    @JsonIgnore
    private Set<User> favoritedByUsers;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(Integer activityDuration) {
        this.activityDuration = activityDuration;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(String activityDetail) {
        this.activityDetail = activityDetail;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Integer getCategoryLevel1() {
        return categoryLevel1;
    }

    public void setCategoryLevel1(Integer categoryLevel1) {
        this.categoryLevel1 = categoryLevel1;
    }

    public Integer getCategoryLevel2() {
        return categoryLevel2;
    }

    public void setCategoryLevel2(Integer categoryLevel2) {
        this.categoryLevel2 = categoryLevel2;
    }

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public CustomerStaff getCustomerStaff() {
        return customerStaff;
    }

    public void setCustomerStaff(CustomerStaff customerStaff) {
        this.customerStaff = customerStaff;
    }

    public Integer getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(Integer verificationType) {
        this.verificationType = verificationType;
    }

    public String getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(String collaborators) {
        this.collaborators = collaborators;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Set<User> getFavoritedByUsers() {
        return favoritedByUsers;
    }

    public void setFavoritedByUsers(Set<User> favoritedByUsers) {
        this.favoritedByUsers = favoritedByUsers;
    }
}
