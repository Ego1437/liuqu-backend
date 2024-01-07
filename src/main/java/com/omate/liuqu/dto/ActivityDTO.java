package com.omate.liuqu.dto;

import java.util.List;

public class ActivityDTO {

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(Integer activityDuration) {
        this.activityDuration = activityDuration;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public List<String> getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(List<String> activityImage) {
        this.activityImage = activityImage;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public List<String> getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(List<String> activityDetail) {
        this.activityDetail = activityDetail;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    private String eventTime; // 或 LocalDateTime/Date 类型，取决于 JSON 数据格式
    private Integer activityDuration;
    private Integer maxCapacity;
    private Integer activityType;
    private List<String> activityImage; // JSON 或 String 类型取决于具体用例
    private String portfolio; // JSON 或 String 类型取决于具体用例
    private List<String> activityDetail; // JSON 或 String 类型取决于具体用例
    private Integer activityStatus;
    private String activityName;
    // Getters and Setters
}

