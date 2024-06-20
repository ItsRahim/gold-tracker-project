package com.rahim.userservice.config;

import com.rahim.userservice.model.Address;
import com.rahim.userservice.request.account.AccountCreationRequest;
import com.rahim.userservice.request.profile.ProfileCreationRequest;
import com.rahim.userservice.model.UserRequest;
import org.instancio.Instancio;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static Address generateAddress() {
        return new Address("20 Bond Street", "London", "EC1V 3JT", "England");
    }

    public static List<AccountCreationRequest> generateAccountData(int numOfData) {
        return Instancio
                .ofList(AccountCreationRequest.class)
                .size(numOfData)
                .create();
    }

    public static List<ProfileCreationRequest> generateProfileData(int numOfData) {
        return Instancio
                .ofList(ProfileCreationRequest.class)
                .size(numOfData)
                .create();
    }

    public List<UserRequest> generateUserRequestData(int numOfData) {
        List<AccountCreationRequest> accountData = generateAccountData(numOfData);
        List<ProfileCreationRequest> profileData = generateProfileData(numOfData);

        List<UserRequest> userRequests = new ArrayList<>();

        for (int i = 0; i < numOfData; i++) {
            AccountCreationRequest account = accountData.get(i);
            ProfileCreationRequest profile = profileData.get(i);

            UserRequest userRequest = new UserRequest(account, profile);

            userRequests.add(userRequest);
        }

        return userRequests;
    }

    public static UserRequest generateSingleUserRequest() {
        AccountCreationRequest account = Instancio.create(AccountCreationRequest.class);
        ProfileCreationRequest profile = Instancio.create(ProfileCreationRequest.class);

        return new UserRequest(account, profile);
    }

}
