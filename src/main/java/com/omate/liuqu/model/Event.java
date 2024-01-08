package com.omate.liuqu.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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

    @Id // 表示这是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略
    @Column(name = "event_id") // 映射到表中的event_id列
    private Long eventId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "start_time") // 映射到表中的start_time列
    private Date startTime;

    @Column(name = "deadline") // 映射到表中的deadline列
    private Date deadline;

    @Column(name = "max_capacity") // 映射到表中的max_capacity列
    private Integer maxCapacity;

    @Column(name = "residual_num") // 映射到表中的residual_num列
    private Integer residualNum;

    @Column(name = "event_status") // 映射到表中的event_status列
    private Integer eventStatus;



    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Ticket> tickets;
    // 构造函数、getter和setter省略，根据需要添加

}
