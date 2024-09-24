package com.application.crudApplication.service;

import com.application.crudApplication.entity.Audit;
import com.application.crudApplication.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    @Autowired
    private AuditRepository auditRepository;

    public void logAudit(String message) {
        Audit audit = new Audit();
        audit.setMessage(message);
        auditRepository.save(audit);
    }
}
