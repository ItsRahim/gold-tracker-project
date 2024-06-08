package com.rahim.authenticationservice.controller;

import com.rahim.authenticationservice.constant.EncryptionControllerURLConstant;
import com.rahim.authenticationservice.service.IEncryptionService;
import com.rahim.authenticationservice.service.feign.PythonEncryption;
import com.rahim.authenticationservice.util.ResponseEntityFormatter;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(EncryptionControllerURLConstant.ENCRYPTOR_BASE_URL)
@Tag(name = "Encryption Management", description = "Endpoint for encrypting information for Java and Python")
public class EncryptorController {

    private static final Logger log = LoggerFactory.getLogger(EncryptorController.class);
    private final PythonEncryption pythonEncryption;
    private final IEncryptionService encryptionService;

    @Operation(summary = "Encrypt data for Java")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Java data encrypted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "200", description = "Java data encrypted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error encrypting data for Java", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = EncryptionControllerURLConstant.ENCRYPT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> javaEncrypt(@RequestBody Map<String, String> plainTextMap) {
        Map<String, String> encryptedDataMap = encryptionService.encrypt(plainTextMap);
        if (!encryptedDataMap.isEmpty()) {
            log.info("Data encrypted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(encryptedDataMap);
        } else {
            log.error("Encryption failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Encrypt data for Python")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Python data encrypted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Request must contain exactly one key-value pair", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error encrypting data for Python", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = EncryptionControllerURLConstant.PYTHON_ENCRYPT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> pythonEncrypt(@RequestBody Map<String, String> encryptionBody) {
        if (encryptionBody == null) {
            return ResponseEntityFormatter.jsonFormatter(HttpStatus.BAD_REQUEST, "Request must contain at least one key-value pair");
        }
        try {
            Map<String, String> encryptedData = pythonEncryption.encryptPlainText(encryptionBody);
            return ResponseEntity.status(HttpStatus.OK).body(encryptedData);
        } catch (Exception e) {
            return ResponseEntityFormatter.jsonFormatter(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to encrypt data");
        }
    }

}
