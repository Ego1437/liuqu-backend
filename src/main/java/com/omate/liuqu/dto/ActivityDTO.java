package com.omate.liuqu.dto;

import com.omate.liuqu.model.CustomerStaff;
import com.omate.liuqu.model.Event;
import com.omate.liuqu.model.Tag;

import java.util.List;
import java.util.Set;

public class ActivityDTO {

    private Long activityId;
    private String activityAddress;
    private String activityImage;
    private String activityName;
    private Integer activityDuration;
    private String portfolio;
    private String activityDetail;
    private Integer activityStatus;
    private Integer categoryLevel1;
    private Integer categoryLevel2;
    private Set<TagDTO> tags;
    private CustomerStaff customerStaff;
    private Integer verificationType;
    private List<EventDTO> events; // 使用EventDTO替换Event实体
    private String collaborators; // 使用EventDTO替换Event实体
    private Integer fansCount;
    // 可以考虑添加其他您需要的字段

    public ActivityDTO(Long activityId, String activityAddress, String activityImage, String activityName, Integer activityDuration, String portfolio, String activityDetail, Integer activityStatus, Integer categoryLevel1, Integer categoryLevel2, Set<TagDTO> tags, CustomerStaff customerStaff, Integer verificationType, List<EventDTO> events, String collaborators, Integer fansCount) {
        this.activityId = activityId;
        this.activityAddress = activityAddress;
        this.activityImage = activityImage;
        this.activityName = activityName;
        this.activityDuration = activityDuration;
        this.portfolio = portfolio;
        this.activityDetail = activityDetail;
        this.activityStatus = activityStatus;
        this.categoryLevel1 = categoryLevel1;
        this.categoryLevel2 = categoryLevel2;
        this.tags = tags;
        this.customerStaff = customerStaff;
        this.verificationType = verificationType;
        this.events = events;
        this.collaborators = collaborators;
        this.fansCount = fansCount;
    }


    // Getters and setters
    // ...
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
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

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public CustomerStaff getCustomerStaff() {
        return customerStaff;
    }

    public void setCustomerStaff(CustomerStaff customerStaff) {
        this.customerStaff = customerStaff;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
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

    public void setFavoritesCount(Integer fansCount) {
        this.fansCount = fansCount;
    }
}

