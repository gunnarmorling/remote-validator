package org.hibernate.validator.remotemetamodel;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 */
public interface ValidationConfiguration {

    boolean addViolationMessageToResponse();

    boolean addViolationMessageTemplateToResponse();

    Map<Class<? extends Annotation>, String> getConstraintAnnotationMapping();

    Map<String, Class<?>> getTypeMapping();
}
