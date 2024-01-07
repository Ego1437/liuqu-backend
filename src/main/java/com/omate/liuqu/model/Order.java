package com.omate.liuqu.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private Long activityId;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private Integer staffId;

    @Column(nullable = false)
    private Long userId;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal platformDiscount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal finalAmount;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    // getters and setters
}
