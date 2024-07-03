package com.rahim.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.userservice.config.AbstractTestConfig;
import com.rahim.userservice.entity.Account;
import com.rahim.userservice.model.Address;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.rahim.common.util.JsonUtil.convertObjectToJson;
import static com.rahim.userservice.config.TestDataGenerator.generateAddress;
import static com.rahim.userservice.config.TestDataGenerator.generateSingleUserRequest;
import static com.rahim.userservice.constant.AccountControllerEndpoint.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountControllerPOSTTest extends AbstractTestConfig {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Address address = generateAddress();

    private String generateJsonData() {
        UserRequest userRequest = generateSingleUserRequest();
        return convertObjectToJson(userRequest);
    }

    @Test
    @DisplayName("Should Successfully Create New Account")
    void shouldCreateNewAccount() throws Exception {
        String accountData = generateJsonData();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountData))
                .andExpect(status().isCreated())
                .andReturn();

        Object userRequest = objectMapper.readValue(result.getResponse().getContentAsString(), UserRequest.class);
        assertThat(userRequest).isInstanceOf(UserRequest.class);

        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).isNotEmpty();
    }

    @Test
    @DisplayName("Should Successfully Create Two New Accounts")
    void shouldCreateTwoNewAccounts() throws Exception {
        String accountData = generateJsonData();

        MvcResult account1 = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountData))
                .andExpect(status().isCreated())
                .andReturn();

        Object userRequest1 = objectMapper.readValue(account1.getResponse().getContentAsString(), UserRequest.class);
        assertThat(userRequest1).isInstanceOf(UserRequest.class);

        String accountData2 = generateJsonData();
        MvcResult account2 = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountData2))
                .andExpect(status().isCreated())
                .andReturn();

        Object userRequest2 = objectMapper.readValue(account2.getResponse().getContentAsString(), UserRequest.class);
        assertThat(userRequest2).isInstanceOf(UserRequest.class);

        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(2);
    }

    @Test
    @DisplayName("Should Not Create Account - Duplicate")
    void shouldNotCreateDuplicateAccount() throws Exception {
        String accountData = generateJsonData();

        MvcResult unique = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountData))
                .andExpect(status().isCreated())
                .andReturn();

        Object userRequest = objectMapper.readValue(unique.getResponse().getContentAsString(), UserRequest.class);
        assertThat(userRequest).isInstanceOf(UserRequest.class);

        MvcResult duplicate = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountData))
                .andExpect(status().isBadRequest())
                .andReturn();

        Object responseBody = objectMapper.readValue(duplicate.getResponse().getContentAsString(), Object.class);
        assertThat(responseBody).isNotNull();

        List<Account> accountList = accountRepository.findAll();
        assertThat(accountList).isNotEmpty();
        assertThat(accountList).hasSize(1);
    }

    @Test
    @DisplayName("Unable To Create Account - Invalid Values")
    void errorCreatingAccountInvalidValues() throws Exception {
        UserRequest userRequest = new UserRequest(null, null);
        String data = convertObjectToJson(userRequest);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).isNotEmpty();

        List<Account> accountList = accountRepository.findAll();
        assertThat(accountList).isEmpty();
    }

    @Test
    @DisplayName("Error Creating Account - Wrong Object")
    void errorCreatingAccountWrongObject() throws Exception {
        String accountData = "INVALID OBJECT";

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(accountData))
                .andExpect(status().isInternalServerError())
                .andReturn();

        Object responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isNotNull();
    }
}
