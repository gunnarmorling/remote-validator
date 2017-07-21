/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.remotemetamodel;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 */
public class RemoteValidator {

    private final Validator validator;
    private final ClassLoader classLoader;

    public RemoteValidator() {
        this(Validation.buildDefaultValidatorFactory()
                .getValidator(), RemoteValidator.class.getClassLoader());
    }

    public RemoteValidator(final Validator validator, final ClassLoader classLoader) {
        this.validator = Assert.requireNonNull(validator, "validator");
        this.classLoader = Assert.requireNonNull(classLoader, "classLoader");
    }

    public Set<ConstraintViolation<?>> validateValue(final String typeName, final String property, final Object value) {
        Assert.requireNonNull(typeName, "typeName");
        Assert.requireNonNull(property, "property");
        Class clazz;
        try {
            clazz = classLoader.loadClass(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can not find class for type " + typeName, e);
        }
        return validator.validateValue(clazz, property, value);
    }
}
