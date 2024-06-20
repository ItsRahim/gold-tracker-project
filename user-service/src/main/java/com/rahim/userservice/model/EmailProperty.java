package com.rahim.userservice.model;

import com.rahim.common.constant.EmailTemplate;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Rahim Ahmed
 * @created 27/04/2024
 */
@Getter
@Builder
public class EmailProperty {
    private int accountId;
    private EmailTemplate templateName;
    private boolean includeUsername;
    private boolean includeDate;
    private String oldEmail;

}
