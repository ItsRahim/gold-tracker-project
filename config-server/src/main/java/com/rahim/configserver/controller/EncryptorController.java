package com.rahim.configserver.controller;

import com.rahim.configserver.service.IEncryptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rahim.configserver.constant.EncryptionControllerURLConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ENCRYPTOR_BASE_URL)
public class EncryptorController {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptorController.class);

    private final IEncryptionService encryptionService;

    @PostMapping(ENCRYPT)
    public ResponseEntity<String> encrypt(@RequestBody String plainText) {
        String encryptedText = encryptionService.encrypt(plainText);
        if (encryptedText != null) {
            LOG.info("Text encrypted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(encryptedText);
        } else {
            LOG.error("Encryption failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
