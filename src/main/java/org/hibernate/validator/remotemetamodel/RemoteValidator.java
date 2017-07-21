/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.remotemetamodel;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author Gunnar Morling
 *
 */
public class RemoteValidator {

	private final Validator validator;
	private final ClassLoader classLoader;

	public RemoteValidator() {
		validator = Validation.buildDefaultValidatorFactory()
				.getValidator();

		classLoader = RemoteValidator.class.getClassLoader();
	}

	public Set<ConstraintViolation<?>> validateValue(String typeName, String property, Object value) {
		Class clazz;

		try {
			clazz = classLoader.loadClass( typeName );
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException( e );
		}

		return validator.validateValue( clazz, property, value );
	}
}
