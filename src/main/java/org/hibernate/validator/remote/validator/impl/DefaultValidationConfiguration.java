package org.hibernate.validator.remote.validator.impl;

import org.hibernate.validator.remote.validator.RemoteValidationConfiguration;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 *
 */
public class DefaultValidationConfiguration implements RemoteValidationConfiguration {

    private boolean addViolationMessageToResponse = true;

    private boolean addViolationMessageTemplateToResponse = false;

    private final Map<Class<? extends Annotation>, String> constraintAnnotationMapping = new HashMap<>();

    private final Map<String, Class<?>> typeMapping = new HashMap<>();

    public DefaultValidationConfiguration() {
    }

    //TODO: Rename
    public void setAddViolationMessageToResponse(boolean addViolationMessageToResponse) {
        this.addViolationMessageToResponse = addViolationMessageToResponse;
    }

    //TODO: Rename
    public void setAddViolationMessageTemplateToResponse(boolean addViolationMessageTemplateToResponse) {
        this.addViolationMessageTemplateToResponse = addViolationMessageTemplateToResponse;
    }

    @Override
    public boolean addViolationMessageToResponse() {
        return addViolationMessageToResponse;
    }

    @Override
    public boolean addViolationMessageTemplateToResponse() {
        return addViolationMessageTemplateToResponse;
    }

    public void addConstraintAnnotationMapping(final Class<? extends Annotation> cls, final String identifier) {
        constraintAnnotationMapping.put(cls, identifier);
    }

    @Override
    public Map<Class<? extends Annotation>, String> getConstraintAnnotationMapping() {
        return Collections.unmodifiableMap(constraintAnnotationMapping);
    }

    public void addTypeMapping(final String identifier, final Class<?> cls) {
        typeMapping.put(identifier, cls);
    }

    @Override
    public Map<String, Class<?>> getTypeMapping() {
        return Collections.unmodifiableMap(typeMapping);
    }
}
