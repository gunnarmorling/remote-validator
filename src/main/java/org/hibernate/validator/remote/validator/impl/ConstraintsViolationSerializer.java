package org.hibernate.validator.remote.validator.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.hibernate.validator.remote.impl.Assert;
import org.hibernate.validator.remote.impl.RemoteConstants;
import org.hibernate.validator.remote.validator.RemoteValidationConfiguration;

import javax.validation.ConstraintViolation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 *
 */
public class ConstraintsViolationSerializer implements JsonSerializer<ConstraintViolation<?>> {

    private final RemoteValidationConfiguration configuration;

    public ConstraintsViolationSerializer(final RemoteValidationConfiguration configuration) {
        this.configuration = Assert.requireNonNull(configuration, "configuration");
    }

    @Override
    public JsonElement serialize(final ConstraintViolation<?> constraintViolation, final Type type, final JsonSerializationContext jsonSerializationContext) {
        Assert.requireNonNull(constraintViolation, "constraintViolation");
        final JsonObject root = new JsonObject();
        root.addProperty(RemoteConstants.TYPE_PROPERTY_NAME, getTypeName(constraintViolation.getConstraintDescriptor().getAnnotation().annotationType()));
        if(configuration.addViolationMessageToResponse()) {
            root.addProperty(RemoteConstants.MESSAGE_PROPERTY_NAME, constraintViolation.getMessage());
        }
        if(configuration.addViolationMessageTemplateToResponse()) {
            root.addProperty(RemoteConstants.TEMPLATE_PROPERTY_NAME, constraintViolation.getMessageTemplate());
        }
        return root;
    }

    private String getTypeName(final Class<? extends Annotation> annotationClass) {
        Assert.requireNonNull(annotationClass, "annotationClass");
        Map<Class<? extends Annotation>, String> mapping =  configuration.getConstraintAnnotationMapping();
        Assert.requireNonNull(mapping, "mapping");
        if(mapping.containsKey(annotationClass)) {
            return mapping.get(annotationClass);
        }
        return annotationClass.getSimpleName();
    }
}
