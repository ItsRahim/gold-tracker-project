package com.rahim.emailservice.controller;

import com.rahim.emailservice.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gold/email-service")
public class EmailServiceController {
    private final IEmailService emailService;
}
