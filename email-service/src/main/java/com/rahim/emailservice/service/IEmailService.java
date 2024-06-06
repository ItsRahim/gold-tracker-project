package com.rahim.emailservice.service;

import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.common.model.kafka.PriceAlertEmailData;

public interface IEmailService {
    Integer findIdByName(String templateName);
    void sendAccountAlert(AccountEmailData accountData);
    void sendPriceAlert(PriceAlertEmailData priceAlert);
}
