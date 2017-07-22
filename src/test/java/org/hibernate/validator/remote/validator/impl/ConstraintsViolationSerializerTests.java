package org.hibernate.validator.remote.validator.impl;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.constraints.Size;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Created by hendrikebbers on 22.07.17.
 */
public class ConstraintsViolationSerializerTests {

    @Test
    public void testNullValuesInConstructor() {
        try {
            final ConstraintsViolationSerializer constraintsViolationSerializer = new ConstraintsViolationSerializer(null);
            fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }

    @Test
    public void checkNullViolation() {
        final ConstraintsViolationSerializer constraintsViolationSerializer = new ConstraintsViolationSerializer(new DefaultValidationConfiguration());
        try {
            final JsonElement jsonElement = constraintsViolationSerializer.serialize(null, null, null);
            fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }

    @Test
    public void checkSimpleViolation() {
        final RemoteValidator remoteValidator = new RemoteValidator(new DefaultValidationConfiguration(), Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", "1");
        final ConstraintsViolationSerializer constraintsViolationSerializer = new ConstraintsViolationSerializer(new DefaultValidationConfiguration());
        final JsonElement jsonElement = constraintsViolationSerializer.serialize(violations.iterator().next(), null, null);
        Assert.assertNotNull(jsonElement);
        Assert.assertTrue(jsonElement.isJsonObject());
        Assert.assertEquals(2, jsonElement.getAsJsonObject().entrySet().size());
        Assert.assertTrue(jsonElement.getAsJsonObject().has("type"));
        Assert.assertTrue(jsonElement.getAsJsonObject().has("message"));
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").isJsonPrimitive());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().isString());
        Assert.assertEquals("Size", jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().getAsString());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("message").isJsonPrimitive());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("message").getAsJsonPrimitive().isString());
    }

    @Test
    public void checkConfigurationForMessageTemplate() {
        final DefaultValidationConfiguration configuration = new DefaultValidationConfiguration();
        configuration.setAddViolationMessageTemplateToResponse(true);
        final RemoteValidator remoteValidator = new RemoteValidator(configuration, Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", "1");
        final ConstraintsViolationSerializer constraintsViolationSerializer = new ConstraintsViolationSerializer(configuration);
        final JsonElement jsonElement = constraintsViolationSerializer.serialize(violations.iterator().next(), null, null);
        Assert.assertNotNull(jsonElement);
        Assert.assertTrue(jsonElement.isJsonObject());
        Assert.assertEquals(3, jsonElement.getAsJsonObject().entrySet().size());
        Assert.assertTrue(jsonElement.getAsJsonObject().has("type"));
        Assert.assertTrue(jsonElement.getAsJsonObject().has("message"));
        Assert.assertTrue(jsonElement.getAsJsonObject().has("template"));
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").isJsonPrimitive());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().isString());
        Assert.assertEquals("Size", jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().getAsString());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("message").isJsonPrimitive());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("message").getAsJsonPrimitive().isString());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("template").isJsonPrimitive());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("template").getAsJsonPrimitive().isString());
    }

    @Test
    public void checkMinimumConfiguration() {
        final DefaultValidationConfiguration configuration = new DefaultValidationConfiguration();
        configuration.setAddViolationMessageToResponse(false);
        final RemoteValidator remoteValidator = new RemoteValidator(configuration, Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", "1");
        final ConstraintsViolationSerializer constraintsViolationSerializer = new ConstraintsViolationSerializer(configuration);
        final JsonElement jsonElement = constraintsViolationSerializer.serialize(violations.iterator().next(), null, null);
        Assert.assertNotNull(jsonElement);
        Assert.assertTrue(jsonElement.isJsonObject());
        Assert.assertEquals(1, jsonElement.getAsJsonObject().entrySet().size());
        Assert.assertTrue(jsonElement.getAsJsonObject().has("type"));
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").isJsonPrimitive());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().isString());
        Assert.assertEquals("Size", jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().getAsString());
    }

    @Test
    public void checkCustomTypeMapping() {
        final DefaultValidationConfiguration configuration = new DefaultValidationConfiguration();
        configuration.addConstraintAnnotationMapping(Size.class, "NewSizeName");
        final RemoteValidator remoteValidator = new RemoteValidator(configuration, Validation.buildDefaultValidatorFactory().getValidator(), TestData.class.getClassLoader());
        final Set<ConstraintViolation<?>> violations = remoteValidator.validateValue(TestData.class.getName(), "name", "1");
        final ConstraintsViolationSerializer constraintsViolationSerializer = new ConstraintsViolationSerializer(configuration);
        final JsonElement jsonElement = constraintsViolationSerializer.serialize(violations.iterator().next(), null, null);
        Assert.assertNotNull(jsonElement);
        Assert.assertTrue(jsonElement.isJsonObject());
        Assert.assertTrue(jsonElement.getAsJsonObject().has("type"));
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").isJsonPrimitive());
        Assert.assertTrue(jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().isString());
        Assert.assertEquals("NewSizeName", jsonElement.getAsJsonObject().get("type").getAsJsonPrimitive().getAsString());
    }
}
