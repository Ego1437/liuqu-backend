package com.omate.liuqu.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @Column(nullable = false)
    private Long eventId; // 假设这是另一个表的外键

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE) // 使用Temporal，因为架构中的类型指示为 'date'
    private Date deadline;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // getter和setter方法
}
