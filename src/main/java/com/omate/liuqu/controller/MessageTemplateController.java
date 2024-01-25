package com.omate.liuqu.controller;

import com.omate.liuqu.model.MessageTemplate;
import com.omate.liuqu.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message-templates")
public class MessageTemplateController {

    private final MessageTemplateService messageTemplateService;

    @Autowired
    public MessageTemplateController(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }

    @GetMapping
    public ResponseEntity<List<MessageTemplate>> getAllMessageTemplates() {
        return ResponseEntity.ok(messageTemplateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageTemplate> getMessageTemplateById(@PathVariable Long id) {
        return messageTemplateService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MessageTemplate> createMessageTemplate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate savedMessageTemplate = messageTemplateService.save(messageTemplate);
        return ResponseEntity.ok(savedMessageTemplate);
    }

    // Other endpoints...
}
