package com.workable.matchmakers.web.configuration;

import com.workable.matchmakers.web.security.authentication.MatchmakersAuthenticationProvider;
import com.workable.matchmakers.web.security.filters.AuthenticationFilter;
import com.workable.matchmakers.web.security.filters.ExceptionHandlerFilter;
import com.workable.matchmakers.web.security.filters.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] SWAGGER_WHITELIST_REGEX = {"/swagger-resources.*", "/swagger-ui.html", "docs/api.html",
			"/v2/api-docs.*", "/docs.*", "/webjars.*", "/configuration/ui", "/configuration/security"};

	private static final String[] FRONTEND_WHITELIST_REGEX = {"/js.*", "/images.*", "/css.*", "/fonts.*", "/app.*"};

	private static final String[] MATCHMAKERS_WHITELIST_REGEX = new String[]{"/", "/api", "/version", "/health", "/ehcache.*", "/metrics.*"};

	private static final String USERS_API = "/users/**";
	private static final String MOVIES_API = "/movies/**";
	private static final String VOTES_API = "/votes/**";

	private static final String[] ADMIN_API = {"/admin/**"};
	private static final String[] FRONTEND_API = { MOVIES_API, USERS_API, VOTES_API};

	public static final String ROLE_GUEST = "ROLE_GUEST";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";


	@Autowired
    MatchmakersAuthenticationProvider matchmakersAuthenticationProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // generates a 60char hash using random salt!
	}

	@Autowired
	@Override
	public void configure(AuthenticationManagerBuilder auth) {
		 auth.authenticationProvider(matchmakersAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Logging
		http.addFilterBefore(new LoggingFilter(), BasicAuthenticationFilter.class);

		// Exception handling
		http.addFilterAfter(new ExceptionHandlerFilter(), BasicAuthenticationFilter.class);

		// Authentication
		http.addFilterAfter(new AuthenticationFilter(authenticationManager()), ExceptionHandlerFilter.class);

		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
				// Permit Swagger and Metrics APIs
				.regexMatchers(MATCHMAKERS_WHITELIST_REGEX).permitAll()
				.regexMatchers(FRONTEND_WHITELIST_REGEX).permitAll()
				.regexMatchers(SWAGGER_WHITELIST_REGEX).permitAll()

				// Permit Movies list and User Authentication API
				.antMatchers(HttpMethod.GET, "/movies").permitAll()
				.antMatchers(HttpMethod.POST, "/users").permitAll()
				.antMatchers(HttpMethod.GET, "/users/id").permitAll()

				// Authorize rest APIs
				.antMatchers(ADMIN_API).hasRole("ADMIN")
				.antMatchers(ROLE_USER).hasAnyRole("ADMIN", "USER")

				// Authenticate rest APIs
				.anyRequest().authenticated() // implicitly permit with .permitAll()
				.and()
				.httpBasic();
	}

	public static Collection<String> getPublicApis() {
		return Set.of(Arrays.asList(FRONTEND_API))
				.stream()
				.flatMap(Collection::stream)
				.map(api -> api.replace("/**", ".*"))
				.collect(Collectors.toList());
	}
}
