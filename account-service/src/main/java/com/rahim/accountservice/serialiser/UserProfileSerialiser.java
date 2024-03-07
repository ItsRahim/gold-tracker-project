package com.rahim.accountservice.serialiser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rahim.accountservice.model.Profile;

import java.io.IOException;

/**
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@Deprecated
public class UserProfileSerialiser extends JsonSerializer<Profile> {

    public void serialize(Profile profile, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("id", profile.getId());
        jsonGenerator.writeStringField("username", profile.getUsername());
        jsonGenerator.writeStringField("firstName", profile.getFirstName());
        jsonGenerator.writeStringField("lastName", profile.getLastName());
        jsonGenerator.writeStringField("contactNumber", profile.getContactNumber());
        jsonGenerator.writeStringField("address", profile.getAddress());
        jsonGenerator.writeEndObject();
    }
}

