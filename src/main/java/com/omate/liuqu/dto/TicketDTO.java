package com.omate.liuqu.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketDTO {
    private Long ticketId;
    private String name;
    private BigDecimal price;
    private LocalDateTime deadline;
    private Integer maxCapacity;
    private Integer residualNum;

    // 构造函数、getters和setters


    public TicketDTO(Long ticketId, String name, BigDecimal price, LocalDateTime deadline, Integer maxCapacity, Integer residualNum) {
        this.ticketId = ticketId;
        this.name = name;
        this.price = price;
        this.deadline = deadline;
        this.maxCapacity = maxCapacity;
        this.residualNum = residualNum;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getResidualNum() {
        return residualNum;
    }

    public void setResidualNum(Integer residualNum) {
        this.residualNum = residualNum;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
