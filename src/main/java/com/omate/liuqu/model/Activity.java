package com.omate.liuqu.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;

@Entity
@Table(name = "activities")
public class Activity {

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

    public CustomerStaff getStaff() {
        return customerStaff;
    }

    public void setStaff(CustomerStaff staff) {
        this.customerStaff = staff;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;

    // 假设partner_id关联到Partner表的partner_id
    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "partner_id")
    private Partner partner;

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
}
