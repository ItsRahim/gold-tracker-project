package com.rahim.configserver.controller;

import com.rahim.configserver.service.IEncryptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.rahim.configserver.constant.EncryptionControllerURLConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ENCRYPTOR_BASE_URL)
public class EncryptorController {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptorController.class);

    private final IEncryptionService encryptionService;

    @PostMapping(ENCRYPT)
    public ResponseEntity<Map<String, String>> encrypt(@RequestBody Map<String, String> plainTextMap) {
        Map<String, String> encryptedDataMap = encryptionService.encrypt(plainTextMap);
        if (!encryptedDataMap.isEmpty()) {
            LOG.info("Data encrypted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(encryptedDataMap);
        } else {
            LOG.error("Encryption failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
