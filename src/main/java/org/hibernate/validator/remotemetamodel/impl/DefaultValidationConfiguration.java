package org.hibernate.validator.remotemetamodel.impl;

import org.hibernate.validator.remotemetamodel.ValidationConfiguration;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 *
 */
public class DefaultValidationConfiguration implements ValidationConfiguration {

    @Override
    public boolean addViolationMessageToResponse() {
        return true;
    }

    @Override
    public boolean addViolationMessageTemplateToResponse() {
        return false;
    }

    @Override
    public Map<Class<? extends Annotation>, String> getConstraintAnnotationMapping() {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Class<?>> getTypeMapping() {
        return Collections.emptyMap();
    }
}
