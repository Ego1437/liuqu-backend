package com.omate.liuqu.service;

import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.Event;
import com.omate.liuqu.model.MessageTemplate;
import com.omate.liuqu.model.Result;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.EventRepository;
import com.omate.liuqu.repository.MessageTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ActivityRepository activityRepository; // 假设这是管理Activity实体的仓库
    private final MessageTemplateRepository messageTemplateRepository;
    @Autowired
    public EventService(EventRepository eventRepository, ActivityRepository activityRepository, MessageTemplateRepository messageTemplateRepository) {
        this.eventRepository = eventRepository;
        this.activityRepository = activityRepository;
        this.messageTemplateRepository = messageTemplateRepository;
    }

    @Transactional
    public Event createEvent(Event event, Long activityId, Long messageTemplateId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));
        event.setActivity(activity);
        MessageTemplate messageTemplate = messageTemplateRepository.findById(messageTemplateId)
                .orElseThrow(() -> new EntityNotFoundException("messageTemplate not found with id: " + activityId));
        event.setActivity(activity);
        event.setMessageTemplate(messageTemplate);
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
