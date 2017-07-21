/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.remote.validator.impl;

import org.hibernate.validator.remote.impl.Assert;
import org.hibernate.validator.remote.validator.RemoteValidationConfiguration;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Map;
import java.util.Set;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 */
public class RemoteValidator {

    private final Validator validator;
    private final ClassLoader classLoader;
    private final RemoteValidationConfiguration configuration;

    public RemoteValidator(final RemoteValidationConfiguration configuration, final Validator validator, final ClassLoader classLoader) {
        this.configuration = Assert.requireNonNull(configuration, "configuration");
        this.validator = Assert.requireNonNull(validator, "validator");
        this.classLoader = Assert.requireNonNull(classLoader, "classLoader");
    }

    public Set<ConstraintViolation<?>> validateValue(final String typeName, final String property, final Object value) {
        Assert.requireNonNull(typeName, "typeName");
        Assert.requireNonNull(property, "property");
        return validator.validateValue((Class)getClassForType(typeName), property, value);
    }

    private Class<?> getClassForType(final String typeName) {
        Assert.requireNonNull(typeName, "typeName");
        Map<String, Class<?>> mapping = configuration.getTypeMapping();
        if(mapping.containsKey(typeName)) {
            Class<?> cls = mapping.get(typeName);
            if(cls == null) {
                throw new IllegalStateException("null value specified for type " + typeName);
            }
            return cls;
        }
        try {
            return classLoader.loadClass(typeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can not find class for type " + typeName, e);
        }
    }
}
