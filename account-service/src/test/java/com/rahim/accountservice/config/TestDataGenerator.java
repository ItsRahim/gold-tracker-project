package com.rahim.accountservice.config;

import com.rahim.accountservice.dto.AccountRequestDto;
import com.rahim.accountservice.dto.ProfileRequestDto;
import com.rahim.accountservice.model.UserRequest;
import org.instancio.Instancio;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {

    private static TestDataGenerator testDataGenerator = null;

    private TestDataGenerator() {}

    public static TestDataGenerator getInstance() {
        if (testDataGenerator == null) {
            testDataGenerator = new TestDataGenerator();
        }

        return testDataGenerator;
    }

    public List<AccountRequestDto> generateAccountData(int numOfData) {
        return Instancio
                .ofList(AccountRequestDto.class)
                .size(numOfData)
                .create();
    }

    public List<ProfileRequestDto> generateProfileData(int numOfData) {
        return Instancio
                .ofList(ProfileRequestDto.class)
                .size(numOfData)
                .create();
    }

    public List<UserRequest> generateUserRequestData(int numOfData) {
        List<AccountRequestDto> accountData = generateAccountData(numOfData);
        List<ProfileRequestDto> profileData = generateProfileData(numOfData);

        List<UserRequest> userRequests = new ArrayList<>();

        for (int i = 0; i < numOfData; i++) {
            AccountRequestDto account = accountData.get(i);
            ProfileRequestDto profile = profileData.get(i);

            UserRequest userRequest = new UserRequest(account, profile);

            userRequests.add(userRequest);
        }

        return userRequests;
    }

    public UserRequest generateSingleUserRequest() {
        AccountRequestDto account = Instancio.create(AccountRequestDto.class);
        ProfileRequestDto profile = Instancio.create(ProfileRequestDto.class);

        return new UserRequest(account, profile);
    }

}
