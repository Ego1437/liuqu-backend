package com.omate.liuqu.dto;

public class PaymentIntentDTO {
    private String id;
    private Long amount;
    private String currency;
    private String status;
    private Long created;

    // 构造器


    public PaymentIntentDTO(String id, Long amount, String currency, String status, Long created) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.created = created;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    // Getter和Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

