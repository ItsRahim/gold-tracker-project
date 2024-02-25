package com.rahim.accountservice.serialiser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rahim.accountservice.model.Account;

import java.io.IOException;

@Deprecated
public class UserSerialiser extends JsonSerializer<Account> {

    public void serialize(Account account, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("user_id", account.getId());
        jsonGenerator.writeStringField("email", account.getEmail());
        jsonGenerator.writeStringField("account_status", account.getAccountStatus());
        jsonGenerator.writeBooleanField("account_locked", account.getAccountLocked());
        jsonGenerator.writeBooleanField("credentials_expired", account.getCredentialsExpired());

        if (account.getLastLogin() != null) {
            jsonGenerator.writeStringField("last_login", account.getLastLogin().toString());
        }

        jsonGenerator.writeBooleanField("notification_setting", account.getNotificationSetting());
        jsonGenerator.writeStringField("created_at", account.getCreatedAt().toString());
        jsonGenerator.writeStringField("updated_at", account.getUpdatedAt().toString());
        jsonGenerator.writeNumberField("login_attempts", account.getLoginAttempts());

        if (account.getDeleteDate() != null) {
            jsonGenerator.writeStringField("delete_date", account.getDeleteDate().toString());
        }

        jsonGenerator.writeEndObject();
    }
}