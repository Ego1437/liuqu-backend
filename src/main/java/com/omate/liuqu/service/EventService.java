package com.omate.liuqu.service;

import com.omate.liuqu.dto.UserDTO;
import com.omate.liuqu.model.Event;
import com.omate.liuqu.model.User;
import com.omate.liuqu.repository.EventRepository;
import com.omate.liuqu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    @Autowired
    public EventService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event, UserDTO userDTO){
        Long uid = userDTO.getUserId();
        User user = userRepository.findById(uid).orElse(null);
        eventRepository.save(event);
        return event;
    }

    public boolean deleteEvent(Long eid, Long uid) {
        Optional<Event> event = eventRepository.findById(eid);
        if (event.isPresent() && event.get().getUid().equals(uid)) {
            eventRepository.deleteById(eid);
            return true;
        }
        return false;
    }

    public boolean existsById(Long eid) {
        return eventRepository.existsById(eid);
    }


    public List<Event> getAllEvents() {
        List<Event> Events = eventRepository.findAll();
        return Events;
    }
}
