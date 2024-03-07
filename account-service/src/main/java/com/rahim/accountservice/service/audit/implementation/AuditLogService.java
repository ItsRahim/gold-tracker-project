package com.rahim.accountservice.service.audit.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.AuditLog;
import com.rahim.accountservice.model.AuditLogData;
import com.rahim.accountservice.repository.AuditLogRepository;
import com.rahim.accountservice.service.audit.IAuditLog;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@Deprecated
@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLog {
    private final AuditLogRepository auditLogRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AuditLogService.class);

    @Override
    public void initialise(Account oldAccount, Account newAccount, String action) throws JsonProcessingException {
        AuditLogData auditLogData = new AuditLogData(oldAccount, newAccount, action);
        updateAuditLog(auditLogData);
    }

    private void updateAuditLog(AuditLogData auditLogData) throws JsonProcessingException {
        int userId = auditLogData.getOldAccount().getId();
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ArrayList<String> auditData = convertToJson(auditLogData.getOldAccount(), auditLogData.getNewAccount());

        AuditLog auditLog = new AuditLog(userId, auditLogData.getActionPerformed(), now, auditData.get(0), auditData.get(1));

        auditLogRepository.save(auditLog);
        LOG.info("Audit Log created for user with ID: {}", userId);
    }

    private ArrayList<String> convertToJson(Account oldAccount, Account newAccount) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String oldUserJson = mapper.writeValueAsString(oldAccount);
        String newUserJson = mapper.writeValueAsString(newAccount);

        return new ArrayList<>(List.of(oldUserJson, newUserJson));
    }
}