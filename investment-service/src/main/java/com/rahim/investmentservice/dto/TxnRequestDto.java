package com.rahim.investmentservice.dto;

import com.rahim.investmentservice.enums.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public class TxnRequestDto {

    private Integer accountId;
    private String goldTypeName;
    private Integer quantity;
    private TransactionType transactionType;
    private BigDecimal transactionPrice;
    private OffsetDateTime transactionDate;
}
