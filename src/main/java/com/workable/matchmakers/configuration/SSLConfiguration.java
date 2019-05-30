package com.workable.matchmakers.configuration;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Add, programmatically, a second Tomcat Connector to handle plain HTTP traffic too
 */
@Configuration
public class SSLConfiguration {

    @Value("${server.port}")
    private int sslPort;

    @Value("${server.http.port}")
    private int connectorPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };

        tomcat.addAdditionalTomcatConnectors(httpConnector()); // redirectConnector()

        return tomcat;
    }

    // Plain HTTP connector without SSL
    private Connector httpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(connectorPort);
        connector.setSecure(false);
        return connector;
    }

    // Redirects the HTTP traffic to HTTPS connector
    private Connector redirectConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(connectorPort);
        connector.setSecure(false);
        connector.setRedirectPort(sslPort); // HTTPS Connector

        return connector;
    }
}
