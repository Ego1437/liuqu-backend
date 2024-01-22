package com.omate.liuqu.service;

import com.omate.liuqu.model.MessageTemplate;
import com.omate.liuqu.repository.MessageTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageTemplateService {

    private final MessageTemplateRepository messageTemplateRepository;

    @Autowired
    public MessageTemplateService(MessageTemplateRepository messageTemplateRepository) {
        this.messageTemplateRepository = messageTemplateRepository;
    }

    public List<MessageTemplate> findAll() {
        return messageTemplateRepository.findAll();
    }

    public Optional<MessageTemplate> findById(Long id) {
        return messageTemplateRepository.findById(id);
    }

    public MessageTemplate save(MessageTemplate messageTemplate) {
        return messageTemplateRepository.save(messageTemplate);
    }

    // Other methods...
}
