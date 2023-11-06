package com.rahim.userservice.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rahim.userservice.model.UserProfile;

import java.io.IOException;

@Deprecated
public class UserProfileSerialiser extends JsonSerializer<UserProfile> {

    public void serialize(UserProfile userProfile, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("id", userProfile.getId());
        jsonGenerator.writeStringField("username", userProfile.getUsername());
        jsonGenerator.writeStringField("firstName", userProfile.getFirstName());
        jsonGenerator.writeStringField("lastName", userProfile.getLastName());
        jsonGenerator.writeStringField("contactNumber", userProfile.getContactNumber());
        jsonGenerator.writeStringField("address", userProfile.getAddress());
        jsonGenerator.writeEndObject();
    }
}

