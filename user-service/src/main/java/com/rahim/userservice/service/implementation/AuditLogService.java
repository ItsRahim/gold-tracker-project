package com.rahim.userservice.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.userservice.model.AuditLog;
import com.rahim.userservice.model.AuditLogData;
import com.rahim.userservice.model.User;
import com.rahim.userservice.repository.AuditLogRepository;
import com.rahim.userservice.service.IAuditLog;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLog {
    private final AuditLogRepository auditLogRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AuditLogService.class);

    @Override
    public void initialise(User oldUser, User newUser, String action) throws JsonProcessingException {
        AuditLogData auditLogData = new AuditLogData(oldUser, newUser, action);
        updateAuditLog(auditLogData);
    }

    private void updateAuditLog(AuditLogData auditLogData) throws JsonProcessingException {
        int userId = auditLogData.getOldUser().getId();
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ArrayList<String> auditData = convertToJson(auditLogData.getOldUser(), auditLogData.getNewUser());

        AuditLog auditLog = new AuditLog(userId, auditLogData.getActionPerformed(), now, auditData.get(0), auditData.get(1));

        auditLogRepository.save(auditLog);
        LOG.info("Audit Log created for user with ID: {}", userId);
    }

    private ArrayList<String> convertToJson(User oldUser, User newUser) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String oldUserJson = mapper.writeValueAsString(oldUser);
        String newUserJson = mapper.writeValueAsString(newUser);

        return new ArrayList<>(List.of(oldUserJson, newUserJson));
    }
}