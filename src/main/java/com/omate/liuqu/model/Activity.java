package com.omate.liuqu.model;

import jakarta.persistence.*;

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
        return staff;
    }

    public void setStaff(CustomerStaff staff) {
        this.staff = staff;
    }


    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
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
    private CustomerStaff staff;

//    @ElementCollection
//    private List<Integer> tags; // 如果tags是一个简单的逗号分隔的字符串列表
    private Integer activityType;

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

    @OneToMany(mappedBy = "activity")
    private List<Event> events;
}
