package com.omate.liuqu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Tickets")
public class Ticket {

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private String name;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "event_id", nullable = false, referencedColumnName = "event_id")
    private Event event;

    @Column(name = "max_capacity") // 映射到表中的max_capacity列
    private Integer maxCapacity;

    @Column(name = "residual_num") // 映射到表中的residual_num列
    private Integer residualNum;
    // getter和setter方法
}
