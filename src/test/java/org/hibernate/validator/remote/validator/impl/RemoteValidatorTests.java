package org.hibernate.validator.remote.validator.impl;

import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

import static org.junit.Assert.*;

public class RemoteValidatorTests {

    @Test
    public void testNullValuesInConstructor() {
        try {
            final RemoteValidator remoteValidator = new RemoteValidator(null, Validation.buildDefaultValidatorFactory().getValidator(), RemoteValidatorTests.class.getClassLoader());
            fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), null, RemoteValidatorTests.class.getClassLoader());
            fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), Validation.buildDefaultValidatorFactory().getValidator(), null);
            fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }

    @Test
    public void validateValidProperty() {
        final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", "12345");
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void validateInvalidProperty() {
        final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", "1");
        assertNotNull(violations);
        assertEquals(1, violations.size());
    }

    @Test
    public void validateInvalidPropertyWithNullValue() {
        final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", null);
        assertNotNull(violations);
        assertEquals(1, violations.size());
    }

    @Test
    public void validateUnknownClass() {
        final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        try {
            remoteValidator.validateValue("com.sample.not.on.classpath.Class", "name", "123");
            fail();
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
            assertNotNull(e.getCause());
            assertEquals(ClassNotFoundException.class, e.getCause().getClass());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void validateUnknownProperty() {
        final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        try {
            remoteValidator.validateValue(TestData.class.getName(), "unknownProperty", "123");
            fail();
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void validatePropertyInMappedClass() {
        final DefaultValidationConfiguration configuration = new DefaultValidationConfiguration();
        configuration.addTypeMapping("testData", TestData.class);
        final RemoteValidator remoteValidator = new RemoteValidator(configuration, Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue("testData", "name", "1");
        assertNotNull(violations);
        assertEquals(1, violations.size());
    }

    @Test
    public void classNameStillWorkingForMappedClass() {
        final DefaultValidationConfiguration configuration = new DefaultValidationConfiguration();
        configuration.addTypeMapping("testData", TestData.class);
        final RemoteValidator remoteValidator = new RemoteValidator(configuration, Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", "1");
        assertNotNull(violations);
        assertEquals(1, violations.size());
    }
}
