package org.hibernate.validator.remotemetamodel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Type;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 *
 */
public class ConstraintsViolationSerializer implements JsonSerializer<ConstraintViolation<?>> {

    @Override
    public JsonElement serialize(final ConstraintViolation<?> constraintViolation, final Type type, final JsonSerializationContext jsonSerializationContext) {
        Assert.requireNonNull(constraintViolation, "constraintViolation");
        final JsonObject root = new JsonObject();
        root.addProperty("message", constraintViolation.getMessage());
        root.addProperty("messageTemplate", constraintViolation.getMessageTemplate());
        return root;
    }
}
