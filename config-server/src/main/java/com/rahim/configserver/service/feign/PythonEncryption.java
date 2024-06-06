package com.rahim.configserver.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 22/04`/2024
 */
@FeignClient(name = "python-api", url = "${python-api.url}")
public interface PythonEncryption {

    /**
     * Sends a POST request to get the gold price.
     *
     * @param requestBody The request body to be sent with the request.
     * @return The response from the server with encrypted data
     */
    @PostMapping("/${python-api.encrypt}")
    Map<String, String> encryptPlainText(@RequestBody Map<String, String> requestBody);

}