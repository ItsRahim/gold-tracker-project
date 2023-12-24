INSERT INTO rgts.email_template(template_name, subject, body, placeholders)
VALUES (
        'Gold Price Alert',
        'Gold Price Alert: Threshold Reached',
        'Dear @firstName @lastName,\n' ||
        '\nWe hope this message finds you well. This is a notification regarding the gold price at our service.\n' ||
        'We want to inform you that the price of gold per ounce has reached a threshold value. As of @alertDateTime, the current price is £@thresholdPrice per ounce.\n' ||
        'If you have any concerns or questions, please review your account for more details. We understand the importance of staying informed about market changes, and we are here to assist you with any queries you may have.\n' ||
        '\nThank you for choosing our service.',
        ARRAY['@firstName', '@lastName', '@thresholdPrice', '@alertDateTime']
       );