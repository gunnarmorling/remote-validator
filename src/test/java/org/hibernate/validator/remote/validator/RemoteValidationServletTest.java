package org.hibernate.validator.remote.validator;

import org.hibernate.validator.remote.validator.impl.DefaultValidationConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by hendrikebbers on 22.07.17.
 */
public class RemoteValidationServletTest {

    @Test
    public void testValidProperty() {
        final String input = "{'type':{'identifier':'org.hibernate.validator.remote.validator.impl.TestData'}, 'instance':{'name':'1234'}}";
        Assert.assertEquals("{}", send(input));
    }

    @Test
    public void testInvalidProperty() {
        final String input = "{'type':{'identifier':'org.hibernate.validator.remote.validator.impl.TestData'}, 'instance':{'name':'1'}}";
        Locale.setDefault(Locale.ENGLISH);
        Assert.assertEquals("{\"name\":[{\"type\":\"Size\",\"message\":\"size must be between 3 and 6\"}]}", send(input));
        Locale.setDefault(Locale.GERMAN);
        Assert.assertEquals("{\"name\":[{\"type\":\"Size\",\"message\":\"muss zwischen 3 und 6 liegen\"}]}", send(input));
    }

    @Test
    public void testInvalidPropertyWithConfiguration() {
        final DefaultValidationConfiguration configuration = new DefaultValidationConfiguration();
        configuration.setAddViolationMessageToResponse(false);
        configuration.setAddViolationMessageTemplateToResponse(true);
        final String input = "{'type':{'identifier':'org.hibernate.validator.remote.validator.impl.TestData'}, 'instance':{'name':'1'}}";
        Assert.assertEquals("{\"name\":[{\"type\":\"Size\",\"template\":\"{javax.validation.constraints.Size.message}\"}]}", send(configuration, input));
    }

    @Test
    public void testUnknownType() {
        final String input = "{'type':{'identifier':'com.test.type.not.known.Class'}, 'instance':{'name':'1234'}}";
        try {
            send(input);
            Assert.fail();
        } catch (Exception e) { }
    }

    @Test
    public void testUnknownProperty() {
        final String input = "{'type':{'identifier':'org.hibernate.validator.remote.validator.impl.TestData'}, 'instance':{'notKnownProperty':'1'}}";
        try {
            send(input);
            Assert.fail();
        } catch (Exception e) { }
    }

    private String send(final String inputJson) {
        return send(new DefaultValidationConfiguration(), inputJson);
    }

    private String send(final RemoteValidationConfiguration configuration, final String inputJson) {
        try {
            final RemoteValidationServlet servlet = new RemoteValidationServlet(configuration);
            final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
            final BufferedReader requestReader = Mockito.mock(BufferedReader.class);
            final Writer responseWriter = new CharArrayWriter();
            Mockito.when(requestReader.lines()).thenReturn(Collections.singleton(inputJson).stream());
            Mockito.when(request.getReader()).thenReturn(requestReader);
            Mockito.when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
            servlet.doPost(request, response);
            return responseWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
