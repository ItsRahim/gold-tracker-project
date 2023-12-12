package com.rahim.emailservice.controller;

import com.rahim.emailservice.service.IEmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gold/email-service")
public class EmailTemplateController {
//    private final IEmailTemplate emailTemplateService;
//    @GetMapping
//    public ResponseEntity<?> something() {
//        Optional<EmailTemplate> optionalEmailTemplate = emailTemplateRepository.findById(1);
//        if (optionalEmailTemplate.isPresent()) {
//            EmailTemplate emailTemplate = optionalEmailTemplate.get();
//            EmailUtil templateUtil = EmailUtil.create(
//                    emailTemplate.getBody(),
//                    emailTemplate.getPlaceholders(),
//                    Arrays.asList("Rahim", "Ahmed", "ItsRahim")
//            );
//
//            if (!templateUtil.isValid()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email Template");
//            }
//
//            return ResponseEntity.status(HttpStatus.OK).body(emailTemplate.getPlaceholders());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email Template not found");
//        }
//    }

}
