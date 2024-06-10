package com.rahim.configserver.controller;

import com.rahim.configserver.service.PythonEncryption;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.rahim.configserver.constant.EncryptionControllerURLConstant.ENCRYPTOR_BASE_URL;
import static com.rahim.configserver.constant.EncryptionControllerURLConstant.PYTHON_ENCRYPT;

@RestController
@RequiredArgsConstructor
@RequestMapping(ENCRYPTOR_BASE_URL)
@Tag(name = "Encryption Management", description = "Endpoint for encrypting information for Java and Python")
public class EncryptorController {

    private static final Logger log = LoggerFactory.getLogger(EncryptorController.class);
    private final PythonEncryption pythonEncryption;

    @Operation(summary = "Encrypt data for Python")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Python data encrypted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Request must contain exactly one key-value pair", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error encrypting data for Python", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = PYTHON_ENCRYPT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> pythonEncrypt(@RequestBody Map<String, String> encryptionBody) {
        if (encryptionBody == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request must contain at least one key-value pair");
        }

        try {
            Map<String, String> encryptedData = pythonEncryption.encryptPlainText(encryptionBody);
            return ResponseEntity.status(HttpStatus.OK).body(encryptedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to encrypt data");
        }
    }

}
