package com.rahim.configserver.controller;

import com.rahim.configserver.service.IEncryptorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rahim.configserver.constants.EncryptionURLConstants.BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 09/03/2024
 */
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class EncryptionApi {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionApi.class);
    private final IEncryptorService encryptorService;

    @PostMapping()
    public ResponseEntity<String> encryptPlaintext(@RequestBody String plaintext) {
        LOG.trace("Received plaintext for encryption: {}", plaintext);
        String[] requestData = plaintext.split("\n");

        if (requestData.length > 1) {
            LOG.warn("Multiple lines received. Encryption will not be performed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only single-line plaintext is allowed.");
        }

        String encryptedValue = encryptorService.encrypt(plaintext);

        if (encryptedValue == null) {
            LOG.warn("Invalid plaintext received: {}", plaintext);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Plaintext must be a non-empty string");
        }

        LOG.info("Plaintext successfully encrypted");
        return ResponseEntity.status(HttpStatus.OK).body(encryptedValue);
    }

}
