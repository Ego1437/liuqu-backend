package com.omate.liuqu.service;

import com.omate.liuqu.dto.TicketDTO;
import com.omate.liuqu.model.Event;
import com.omate.liuqu.model.Ticket;
import com.omate.liuqu.repository.EventRepository;
import com.omate.liuqu.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository; // 确保你有一个EventRepository

    public List<TicketDTO> getTicketsForEvent(Long eventId) {
        List<Ticket> tickets = ticketRepository.findByEventEventId(eventId);
        return tickets.stream().map(ticket -> new TicketDTO(
                ticket.getTicketId(),
                ticket.getName(),
                ticket.getPrice(),
                ticket.getDeadline(),
                ticket.getMaxCapacity(),
                ticket.getResidualNum()
                // ... 其他Ticket属性
        )).collect(Collectors.toList());
    }

    @Autowired
    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Ticket createTicket(Ticket ticket, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        ticket.setEvent(event);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional
    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}
