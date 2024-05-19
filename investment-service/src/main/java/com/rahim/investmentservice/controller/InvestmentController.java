package com.rahim.investmentservice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rahim.investmentservice.constants.InvestmentControllerEndpoint.BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class InvestmentController {

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentController.class);
}
