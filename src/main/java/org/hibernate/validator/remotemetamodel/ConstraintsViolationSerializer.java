package org.hibernate.validator.remotemetamodel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Type;

public class ConstraintsViolationSerializer implements JsonSerializer<ConstraintViolation<?>> {

    @Override
    public JsonElement serialize(ConstraintViolation<?> constraintViolation, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject root = new JsonObject();
        root.addProperty("message", constraintViolation.getMessage());
        root.addProperty("messageTemplate", constraintViolation.getMessageTemplate());
        return root;
    }
}
