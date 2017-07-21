package org.hibernate.validator.remotemetamodel;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 */
public class ValidationConfiguration {


    public boolean addViolationMessageToResponse() {
        return true;
    }

    public boolean addViolationMessageTemplateToResponse() {
        return true;
    }

    public Map<Class<? extends Annotation>, String> getContraintsAnnotationMapping() {
        return Collections.emptyMap();
    }

    public Map<String, Class<?>> getTypeMapping() {
        return Collections.emptyMap();
    }
}
