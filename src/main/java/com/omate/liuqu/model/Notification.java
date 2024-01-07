package com.omate.liuqu.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(length = 50, nullable = false)
    private String userId;

    @Column(length = 30, nullable = false)
    private String userEmail;

    @Column(length = 20, nullable = false)
    private String activityId;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date eventTime;

    @Column(length = 100)
    private String promotion;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

    // getter和setter方法
}

