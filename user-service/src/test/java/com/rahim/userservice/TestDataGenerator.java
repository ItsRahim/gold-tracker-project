package com.rahim.userservice;

import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.Profile;
import com.rahim.userservice.model.UserRequest;
import org.instancio.Instancio;

import java.util.ArrayList;
import java.util.List;

import static org.instancio.Select.field;

public class TestDataGenerator {

    public static TestDataGenerator getInstance() {
        return new TestDataGenerator();
    }

    public List<Account> generateAccountData(int numOfData) {
        return Instancio
                .ofList(Account.class)
                .size(numOfData)
                .ignore(field(Account::getId))
                .ignore(field(Account::getAccountStatus))
                .ignore(field(Account::getCredentialsExpired))
                .ignore(field(Account::getLastLogin))
                .ignore(field(Account::getNotificationSetting))
                .ignore(field(Account::getCreatedAt))
                .ignore(field(Account::getUpdatedAt))
                .ignore(field(Account::getAccountLocked))
                .ignore(field(Account::getAccountStatus))
                .create();
    }

    public List<Profile> generateProfileData(int numOfData) {
        return Instancio
                .ofList(Profile.class)
                .size(numOfData)
                .ignore(field(Profile::getId))
                .ignore(field(Profile::getAccount))
                .create();
    }

    public List<UserRequest> generateUserRequestData(int numOfData) {
        List<Account> accountData = generateAccountData(numOfData);
        List<Profile> profileData = generateProfileData(numOfData);

        List<UserRequest> userRequests = new ArrayList<>();

        for (int i = 0; i < numOfData; i++) {
            Account account = accountData.get(i);
            Profile profile = profileData.get(i);

            UserRequest userRequest = new UserRequest(account, profile);

            userRequests.add(userRequest);
        }

        return userRequests;
    }

}
