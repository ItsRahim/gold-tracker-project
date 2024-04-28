package com.rahim.accountservice.model;

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
    private String templateName;
    private boolean includeUsername;
    private boolean includeDate;
    private String oldEmail;

}
