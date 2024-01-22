package com.omate.liuqu.dto;

import java.math.BigDecimal;

public class PaymentNotificationDTO {
    private String returnCode;
    private String nonceStr;
    private Long timestamp;
    private String sign;
    private String orderNo;
    private String outOrderNo;
    private String currency;
    private Integer totalAmount;
    private String orderTime;
    private String payTime;
    private BigDecimal exchangeRate;
    private Integer cnyAmount;

    // Getters and Setters
    // 注意：变量名应该与JSON键值完全对应，或者使用 @JsonProperty 注解来匹配不同的名字。

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getCnyAmount() {
        return cnyAmount;
    }

    public void setCnyAmount(Integer cnyAmount) {
        this.cnyAmount = cnyAmount;
    }
}
