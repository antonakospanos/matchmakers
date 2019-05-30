package com.workable.matchmakers.configuration;

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

import static com.workable.matchmakers.web.api.MatchmakersBaseController.API_BASE;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] SWAGGER_WHITELIST_REGEX = {"/swagger-resources.*", "/swagger-ui.html", "docs/api.html",
			"/v2/api-docs.*", "/docs.*", "/webjars.*", "/configuration/ui", "/configuration/security"};

	private static final String[] VENUS_WHITELIST_REGEX = new String[]{"/", "/version", "/health", "/ehcache.*", "/metrics.*"};

	private static final String CANDIDATES_API = API_BASE + "/candidates/**";
	private static final String[] CANDIDATE_API = { CANDIDATES_API };
	private static final String[] FRONTEND_API = { CANDIDATES_API };
	private static final String[] ADMIN_API = {"/admin/**"};

	public static final String ROLE_GUEST = "ROLE_GUEST";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_CANDIDATE = "ROLE_CANDIDATE";


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

		http.cors().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				// Permit Swagger and Metrics APIs
				.regexMatchers(VENUS_WHITELIST_REGEX).permitAll()
				.regexMatchers(SWAGGER_WHITELIST_REGEX).permitAll()

				// Permit User Authentication API
				.antMatchers(HttpMethod.POST, API_BASE + "/candidates").permitAll()
				.antMatchers(HttpMethod.GET, API_BASE + "/candidates/id").permitAll()
				.antMatchers(HttpMethod.DELETE, API_BASE + "/candidates/password").permitAll()

				// Authorize rest APIs
				.antMatchers(ADMIN_API).hasRole("ADMIN")
				.antMatchers(CANDIDATE_API).hasAnyRole("ADMIN", "CANDIDATE")

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
