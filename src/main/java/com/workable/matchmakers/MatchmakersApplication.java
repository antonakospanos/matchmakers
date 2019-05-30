package com.workable.matchmakers;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
// @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
// @ComponentScan(basePackages = {"com.workable.matchmakers"})
public class MatchmakersApplication extends SpringBootServletInitializer {

	private static final String CONFIG_NAME = "matchmakers-application";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.properties(ImmutableMap.of("spring.config.name", CONFIG_NAME));
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(MatchmakersApplication.class).
				properties(ImmutableMap.of("spring.config.name", CONFIG_NAME))
				.build()
				.run(args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // generates a 60char hash using random salt!
	}
}
