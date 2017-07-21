package org.hibernate.validator.remotemetamodel;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

/**
 * Created by hendrikebbers on 21.07.17.
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
}
