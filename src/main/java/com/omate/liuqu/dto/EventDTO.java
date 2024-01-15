package com.omate.liuqu.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventDTO {
    private Long eventId;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private Integer maxCapacity;
    private Integer residualNum;
    private Integer eventStatus;
    private List<TicketDTO> tickets;

    // 构造函数、getters和setters

    public EventDTO(Long eventId, LocalDateTime startTime, LocalDateTime deadline, Integer maxCapacity, Integer residualNum, Integer eventStatus) {
        this.eventId = eventId;
        this.startTime = startTime;
        this.deadline = deadline;
        this.maxCapacity = maxCapacity;
        this.residualNum = residualNum;
        this.eventStatus = eventStatus;
//        this.tickets = tickets;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
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

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }
}
