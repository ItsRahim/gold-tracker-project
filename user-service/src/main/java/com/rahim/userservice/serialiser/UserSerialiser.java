package com.rahim.userservice.serialiser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rahim.userservice.model.User;

import java.io.IOException;

@Deprecated
public class UserSerialiser extends JsonSerializer<User> {

    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("user_id", user.getId());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeStringField("account_status", user.getAccountStatus());
        jsonGenerator.writeBooleanField("account_locked", user.getAccountLocked());
        jsonGenerator.writeBooleanField("credentials_expired", user.getCredentialsExpired());

        if (user.getLastLogin() != null) {
            jsonGenerator.writeStringField("last_login", user.getLastLogin().toString());
        }

        jsonGenerator.writeBooleanField("notification_setting", user.getNotificationSetting());
        jsonGenerator.writeStringField("created_at", user.getCreatedAt().toString());
        jsonGenerator.writeStringField("updated_at", user.getUpdatedAt().toString());
        jsonGenerator.writeNumberField("login_attempts", user.getLoginAttempts());

        if (user.getDeleteDate() != null) {
            jsonGenerator.writeStringField("delete_date", user.getDeleteDate().toString());
        }

        jsonGenerator.writeEndObject();
    }
}