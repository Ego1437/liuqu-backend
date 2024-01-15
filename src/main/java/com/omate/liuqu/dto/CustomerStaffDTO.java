package com.omate.liuqu.dto;

public class CustomerStaffDTO {
    private Integer customerStaffId;
    private String staffName;
    private String staffTelephone;
    private String staffEmail;

    // 构造函数
    public CustomerStaffDTO(Integer customerStaffId, String staffName,
                            String staffTelephone, String staffEmail
                            ) {
        this.customerStaffId = customerStaffId;
        this.staffName = staffName;
        this.staffTelephone = staffTelephone;
        this.staffEmail = staffEmail;
    }

    // Getters 和 Setters
    public Integer getCustomerStaffId() {
        return customerStaffId;
    }

    public void setCustomerStaffId(Integer customerStaffId) {
        this.customerStaffId = customerStaffId;
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

    // 可以根据需要添加其他方法和逻辑
}
