package org.hibernate.validator.remote.metamodel;

import org.hibernate.validator.remote.impl.Assert;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * Created by hendrikebbers on 21.07.17.
 */
public class RemoteMetamodelServlet extends HttpServlet {

    public static void register(final ServletContext servletContext) {
        register(servletContext, "/validation-model");
    }

    public static void register(final ServletContext servletContext, final String mapping) {
        Assert.requireNonNull(servletContext, "servletContext");
        Assert.requireNonBlank(mapping, "mapping");

        servletContext.addServlet("remoteMetamodelServlet", new RemoteMetamodelServlet()).addMapping(mapping);
    }
}
