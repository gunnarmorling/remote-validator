package org.hibernate.validator.remotemetamodel;

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
        return true;
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
