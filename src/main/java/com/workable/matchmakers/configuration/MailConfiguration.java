package com.workable.matchmakers.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("application.mail")
@EnableConfigurationProperties
@Data
public class MailConfiguration {

    MailConfiguration.Source from;
    MailConfiguration.Target to;

    @Data
    public static class Source {

        String address;
        String host;
        int port;
        String username;
        String password;
    }

    @Data
    public static class Target {

        MailConfiguration.Target.Registration registration;
        MailConfiguration.Target.Interview interview;
        String admins;

        @Data
        public static class Registration {

            String pending;
            String validated;
            String completed;
        }

        @Data
        public static class Interview {

            String requested;
        }
    }
}
