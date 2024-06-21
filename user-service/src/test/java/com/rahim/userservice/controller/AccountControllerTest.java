package com.rahim.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.userservice.config.AbstractTestConfig;
import com.rahim.userservice.config.TestDataGenerator;
import com.rahim.userservice.model.Address;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.request.account.AccountCreationRequest;
import com.rahim.userservice.request.profile.ProfileCreationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.rahim.common.util.JsonUtil.convertObjectToJson;
import static com.rahim.userservice.constant.AccountControllerEndpoint.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountControllerTest extends AbstractTestConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Address address = TestDataGenerator.generateAddress();

    private String generateJsonData() {
        AccountCreationRequest account1 = new AccountCreationRequest("john.doe@gmail.com", "password1!");
        ProfileCreationRequest profile1 = new ProfileCreationRequest("user1", "Brendan", "Estes", "07123456789", address);
        UserRequest userRequest = new UserRequest(account1, profile1);

        return convertObjectToJson(userRequest);
    }

    @Test
    @DisplayName("Should Successfully Create New Account")
    void shouldCreateNewAccount() throws Exception {
        String jsonData = generateJsonData();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isOk())
                .andReturn();

        Object userRequest = objectMapper.readValue(result.getResponse().getContentAsString(), UserRequest.class);
        assertThat(userRequest).isInstanceOf(UserRequest.class);

        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).isNotEmpty();
    }
}
