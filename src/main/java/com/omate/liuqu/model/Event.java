package com.omate.liuqu.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity // 表示这是一个JPA实体
@Table(name = "Events") // 指定对应的数据库表名
public class Event {

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Integer getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Integer eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
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

    @Id // 表示这是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略
    @Column(name = "event_id") // 映射到表中的event_id列
    private Long eventId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "start_time") // 映射到表中的start_time列
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Column(name = "deadline") // 映射到表中的deadline列
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @Column(name = "event_status") // 映射到表中的event_status列
    private Integer eventStatus;

    @Column(name = "max_capacity") // 映射到表中的max_capacity列
    private Integer maxCapacity;

    @Column(name = "residual_num") // 映射到表中的residual_num列
    private Integer residualNum;


    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Ticket> tickets;
    // 构造函数、getter和setter省略，根据需要添加

}
