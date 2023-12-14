package com.rahim.userservice.service;

import com.rahim.userservice.enums.TemplateNameEnum;

public interface IEmailService {
    void generateEmailData(TemplateNameEnum templateName, int userId);
}
