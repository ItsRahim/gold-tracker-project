package com.rahim.investmentservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static com.rahim.investmentservice.constants.HoldingControllerEndpoint.BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Tag(name = "Endpoint to manage user holdings")
public class HoldingController {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingController.class);

}
