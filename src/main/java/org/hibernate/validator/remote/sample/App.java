package org.hibernate.validator.remote.sample;

import org.hibernate.validator.remote.validator.RemoteValidationServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App implements ServletContextInitializer
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        RemoteValidationServlet.register(servletContext);
    }
}
