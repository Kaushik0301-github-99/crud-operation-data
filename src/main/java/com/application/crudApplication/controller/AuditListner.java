package com.application.crudApplication.controller;

import com.application.crudApplication.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class AuditListner {
    @Autowired
    private AuditService auditService;

    @KafkaListener(topics = "person_topic", groupId = "your-group-id")
    public void listen(String message) {
        auditService.logAudit(message);
    }
}
