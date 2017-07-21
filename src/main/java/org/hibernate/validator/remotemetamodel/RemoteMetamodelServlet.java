/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.remotemetamodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hibernate.validator.remotemetamodel.RemoteValidatorConstants.*;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 */
public class RemoteMetamodelServlet extends HttpServlet {

    private final RemoteValidator remoteValidator;
    private final Gson gson;

    public RemoteMetamodelServlet() {
        this(new RemoteValidator(), new ValidationConfiguration());
    }

    public RemoteMetamodelServlet(final RemoteValidator remoteValidator, final ValidationConfiguration configuration) {
        this.remoteValidator = remoteValidator;
        this.gson = new GsonBuilder().registerTypeHierarchyAdapter(ConstraintViolation.class, new ConstraintsViolationSerializer(configuration)).create();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader(CONTENT_TYPE_HEADER_NAME, JSON_CONTENT_TYPE);
        response.setCharacterEncoding(UTF_8);

        /**
         * {
         * 		"config" : {
         * 			"validate-absent-properties" : true,
         * 			"validate-bean" : false
         *        },
         * 		"type": {
         * 			"name" : "a.b.c.Class",
         *        },
         * 		"instance": {
         * 			"id" : 123,
         * 			"name" : "Hendrik"
         *        }
         * }
         */

        String line;
        final StringBuilder requestPayload = new StringBuilder();

        while ((line = request.getReader().readLine()) != null) {
            requestPayload.append(line);
        }

        final JsonObject requestJson = new JsonParser().parse(requestPayload.toString()).getAsJsonObject();

        final String typeName = requestJson
                .get(TYPE_PROPERTY_NAME)
                .getAsJsonObject()
                .get(IDENTIFIER_PROPERTY_NAME)
                .getAsString();

        final JsonObject properties = requestJson.get(INSTANCE_PROPERTY_NAME).getAsJsonObject();
        final Map<String, Set<ConstraintViolation<?>>> violationsByProperty = new HashMap<>();

        properties.keySet().forEach(propertyName -> {
            final JsonElement propertyElement = properties.get(propertyName);
            try {
                final Set<ConstraintViolation<?>> violations = validate(typeName, propertyName, propertyElement);
                if(!violations.isEmpty()) {
                    violationsByProperty.put(propertyName, violations);
                }
            } catch (IllegalArgumentException e) {
                response.setStatus(BAD_REQUEST);
                throw new RuntimeException("Error in validation", e);
            }
        });

        final String responseString = gson.toJson(violationsByProperty);
        response.getWriter().append(responseString);
    }

    private Set<ConstraintViolation<?>> validate(final String typeName, final String propertyName, final JsonElement propertyElement) {
        if (!propertyElement.isJsonPrimitive()) {
            throw new IllegalArgumentException("Only primitive properties supported. Wrong type for " + typeName + "." + propertyName);
        }
        JsonPrimitive primitiveValue = propertyElement.getAsJsonPrimitive();
        if (primitiveValue.isBoolean()) {
            return remoteValidator.validateValue(typeName, propertyName, primitiveValue.getAsBoolean());
        } else if (primitiveValue.isNumber()) {
            return remoteValidator.validateValue(typeName, propertyName, primitiveValue.getAsNumber());
        } else if (primitiveValue.isString()) {
            return remoteValidator.validateValue(typeName, propertyName, primitiveValue.getAsString());
        }
        throw new IllegalArgumentException("Not supported type for " + typeName + "." + propertyName);
    }
}
