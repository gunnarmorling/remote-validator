package org.hibernate.validator.remotemetamodel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javax.validation.ConstraintViolation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import static org.hibernate.validator.remotemetamodel.RemoteValidatorConstants.MESSAGE_PROPERTY_NAME;
import static org.hibernate.validator.remotemetamodel.RemoteValidatorConstants.TEMPLATE_PROPERTY_NAME;
import static org.hibernate.validator.remotemetamodel.RemoteValidatorConstants.TYPE_PROPERTY_NAME;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 *
 */
public class ConstraintsViolationSerializer implements JsonSerializer<ConstraintViolation<?>> {

    private final ValidationConfiguration configuration;

    public ConstraintsViolationSerializer() {
        this(new ValidationConfiguration());
    }

    public ConstraintsViolationSerializer(final ValidationConfiguration configuration) {
        this.configuration = Assert.requireNonNull(configuration, "configuration");
    }

    @Override
    public JsonElement serialize(final ConstraintViolation<?> constraintViolation, final Type type, final JsonSerializationContext jsonSerializationContext) {
        Assert.requireNonNull(constraintViolation, "constraintViolation");
        final JsonObject root = new JsonObject();
        root.addProperty(TYPE_PROPERTY_NAME, getTypeName(constraintViolation.getConstraintDescriptor().getAnnotation().annotationType()));
        if(configuration.addViolationMessageToResponse()) {
            root.addProperty(MESSAGE_PROPERTY_NAME, constraintViolation.getMessage());
        }
        if(configuration.addViolationMessageTemplateToResponse()) {
            root.addProperty(TEMPLATE_PROPERTY_NAME, constraintViolation.getMessageTemplate());
        }
        return root;
    }

    private String getTypeName(final Class<? extends Annotation> annotationClass) {
        Assert.requireNonNull(annotationClass, "annotationClass");
        Map<Class<? extends Annotation>, String> mapping =  configuration.getContraintsAnnotationMapping();
        Assert.requireNonNull(mapping, "mapping");
        if(mapping.containsKey(annotationClass)) {
            return mapping.get(annotationClass);
        }
        return annotationClass.getSimpleName();
    }
}
