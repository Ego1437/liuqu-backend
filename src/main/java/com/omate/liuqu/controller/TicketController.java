package com.omate.liuqu.controller;

import com.omate.liuqu.model.Result;
import com.omate.liuqu.model.Ticket;
import com.omate.liuqu.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/createTicket")
    public ResponseEntity<Result> createTicket(@RequestBody Ticket ticket, @RequestParam("eventId") Long eventId) {
        Ticket createdTicket = ticketService.createTicket(ticket, eventId);
        Result result = new Result();
        result.setResultSuccess(0, createdTicket); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        return ticketService.getTicketById(ticketId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @RequestBody Ticket ticket) {
        if (!ticketService.getTicketById(ticketId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ticket.setTicketId(ticketId);
        Ticket updatedTicket = ticketService.updateTicket(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

}

