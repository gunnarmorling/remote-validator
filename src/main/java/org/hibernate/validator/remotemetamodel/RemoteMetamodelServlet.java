/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.remotemetamodel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * @author Gunnar Morling
 *
 */
public class RemoteMetamodelServlet extends HttpServlet {

	private final RemoteValidator remoteValidator;
	private final Gson gson;

	public RemoteMetamodelServlet() {
		remoteValidator = new RemoteValidator();
		gson = new Gson();
	}

	public RemoteMetamodelServlet(RemoteValidator remoteValidator, Gson gson) {
		this.remoteValidator = remoteValidator;
		this.gson = gson;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader( "Content-Type", "application/json" );
		response.setCharacterEncoding( "UTF-8" );

		/**
		 * {
		 * 		"config" : {
		 * 			"validate-absent-properties" : true,
		 * 			"validate-bean" : false
		 * 		},
		 * 		"type": {
		 * 			"name" : "a.b.c.Class",
		 * 		},
		 * 		"instance": {
		 * 			"id" : 123,
		 * 			"name" : "Hendrik"
		 * 		}
		 * }
		 */

		String line;
		StringBuilder requestPayload = new StringBuilder();

		while( (line = req.getReader().readLine()) != null ) {
			requestPayload.append( line );
		}

		JsonObject requestJson = new JsonParser().parse( requestPayload.toString() ).getAsJsonObject();

		String typeName = requestJson
				.get( "type" )
				.getAsJsonObject()
				.get( "name" )
				.getAsString();

		JsonObject properties = requestJson.get( "instance" ).getAsJsonObject();
		Map<String, Set<ConstraintViolation<?>>> violationsByProperty = new HashMap<>();

		for ( String property : properties.keySet() ) {
			JsonElement propertyValue = properties.get( property );

			if ( !propertyValue.isJsonPrimitive() ) {
				response.setStatus( 400 );
				throw new RuntimeException( "Only primitive properties supported" );
			}

			JsonPrimitive primitiveValue = propertyValue.getAsJsonPrimitive();
			if ( primitiveValue.isBoolean() ) {
				violationsByProperty.put( property, 	remoteValidator.validateValue( typeName, property, primitiveValue.getAsBoolean() ) );
			}
			else if ( primitiveValue.isNumber() ) {
				violationsByProperty.put( property, 	remoteValidator.validateValue( typeName, property, primitiveValue.getAsNumber() ) );
			}
			else if ( primitiveValue.isString() ) {
				violationsByProperty.put( property, 	remoteValidator.validateValue( typeName, property, primitiveValue.getAsString() ) );
			}
		}

		String responseString = gson.toJson( violationsByProperty );
		response.getWriter().append( responseString );
	}
}