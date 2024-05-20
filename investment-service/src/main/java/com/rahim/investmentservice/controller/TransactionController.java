package com.rahim.investmentservice.controller;

import com.rahim.investmentservice.service.transaction.TxnCreationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static com.rahim.investmentservice.constants.TransactionControllerEndpoint.BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Tag(name = "Endpoint to manage user transactions")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private final TxnCreationService txnCreationService;

}
