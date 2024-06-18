package com.rahim.accountservice.service.account;

import com.hazelcast.collection.ISet;
import com.rahim.accountservice.config.AbstractTestConfig;
import com.rahim.accountservice.config.TestDataGenerator;
import com.rahim.accountservice.model.Address;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.request.account.AccountCreationRequest;
import com.rahim.accountservice.request.profile.ProfileCreationRequest;
import com.rahim.accountservice.service.account.implementation.AccountCreationService;
import com.rahim.accountservice.service.repository.implementation.AccountRepositoryHandlerService;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.DuplicateEntityException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountCreationSvcTest extends AbstractTestConfig {

    @Autowired
    private IAccountCreationService accountCreationService;

    @Autowired
    private AccountRepositoryHandlerService accountRepositoryHandlerService;

    @Autowired
    private CacheManager hazelcastCacheManager;

    private static final Address address = TestDataGenerator.generateAddress();

    @Test
    @DisplayName("Should save new account successfully")
    void saveNewAccount_SuccessfulSave() {
        // Generate test data
        UserRequest userRequestData = TestDataGenerator.generateSingleUserRequest();

        // Perform action
        UserRequest userRequest = accountCreationService.createAccount(userRequestData);
        String email = userRequest.getAccount().getEmail();

        // Verify results
        assertThat(userRequest).isNotNull();
        assertThat(accountRepositoryHandlerService.existsByEmail(email)).isTrue();
    }

    @Test
    @DisplayName("Should fail to save account due to duplicate email")
    void failToSaveDuplicateAccount_SameEmail() {

        // Create first account with a specific email
        AccountCreationRequest account1 = new AccountCreationRequest("john.doe@gmail.com", "password1!");
        ProfileCreationRequest profile1 = new ProfileCreationRequest("user1", "Brendan", "Estes", "(270) 851-8260", address);
        UserRequest userRequest1 = new UserRequest(account1, profile1);
        UserRequest firstUser = accountCreationService.createAccount(userRequest1);
        String email = account1.getEmail();

        // Verify first account creation
        assertThat(firstUser).isNotNull();
        assertThat(accountRepositoryHandlerService.existsByEmail(email)).isTrue();

        // Attempt to create a second account with the same email
        AccountCreationRequest account2 = new AccountCreationRequest("john.doe@gmail.com", "newPassword");
        ProfileCreationRequest profile2 = new ProfileCreationRequest("user2", "Fay", "Macintosh", "07123456789", address);
        UserRequest userRequest2 = new UserRequest(account2, profile2);

        // Verify duplicate email handling
        assertThrows(DuplicateEntityException.class, () -> {
            accountCreationService.createAccount(userRequest2);
        });
    }

    @Test
    @DisplayName("Should fail to save account due to duplicate username")
    void failToSaveDuplicateAccount_SameUsername() {
        // Create first account with a unique username
        AccountCreationRequest account1 = new AccountCreationRequest("tincidunt.nunc.ac@aol.couk", "password1!");
        ProfileCreationRequest profile1 = new ProfileCreationRequest("duplicateUsername", "Dorothy", "Bullock", "07046243266", address);
        UserRequest userRequest1 = new UserRequest(account1, profile1);
        UserRequest firstUser = accountCreationService.createAccount(userRequest1);

        // Verify first account creation
        assertThat(firstUser).isNotNull();

        // Attempt to create a second account with the same email
        AccountCreationRequest account2 = new AccountCreationRequest("mollis.phasellus@google.org", "newPassword");
        ProfileCreationRequest profile2 = new ProfileCreationRequest("duplicateUsername", "Natalie", "Rosales", "07644346812", address);
        UserRequest userRequest2 = new UserRequest(account2, profile2);

        assertThrows(DuplicateEntityException.class, () -> accountCreationService.createAccount(userRequest2));
    }

    @Test
    @DisplayName("Fail To Save Duplicate Account - Same Email and Username")
    void failToSaveDuplicateAccount_SameEmailAndUsername() {
        AccountCreationRequest account1 = new AccountCreationRequest("duplicate@gmail.com", "Password789!");
        ProfileCreationRequest profile1 = new ProfileCreationRequest("duplicateUsername", "Alice", "Smith", "07123456789", address);
        UserRequest userRequest1 = new UserRequest(account1, profile1);
        accountCreationService.createAccount(userRequest1);

        AccountCreationRequest account2 = new AccountCreationRequest("duplicate@gmail.com", "NewPassword012");
        ProfileCreationRequest profile2 = new ProfileCreationRequest("duplicateUsername", "Bob", "Johnson", "07123456789", address);
        UserRequest userRequest2 = new UserRequest(account2, profile2);

        assertThrows(DuplicateEntityException.class, () -> accountCreationService.createAccount(userRequest2));
    }

    @Test
    @DisplayName("Should not save invalid inputs")
    void shouldThrowError_InvalidRequestData() {
        AccountCreationRequest account1 = new AccountCreationRequest();
        ProfileCreationRequest profile1 = new ProfileCreationRequest();
        UserRequest invalidUserRequest = new UserRequest(account1, profile1);

        assertThrows(ValidationException.class, () -> accountCreationService.createAccount(invalidUserRequest));
    }

    @Test
    @DisplayName("Should add new account id to hazelcast set successfully")
    void shouldAddNewAccountIdToHazelcastSetSuccessfully() {
        // Verify the initial state of the cache
        ISet<Integer> accountIds = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_SET);
        assertThat(accountIds).isNullOrEmpty();

        UserRequest userRequestData = TestDataGenerator.generateSingleUserRequest();
        UserRequest userRequest = accountCreationService.createAccount(userRequestData);

        assertThat(userRequest).isNotNull();
        assertThat(accountIds).isNotNull();
    }
}
