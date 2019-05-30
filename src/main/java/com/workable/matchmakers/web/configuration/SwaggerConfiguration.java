package com.workable.matchmakers.web.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

//	@Bean(name = "Admin API")
//	public Docket adminApi() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.groupName("Admin API")
//				.apiInfo(adminInfo())
//				.select()
//				.paths(regex(".*/(admin).*"))
//				.build();
//	}
//
//	private ApiInfo adminInfo() {
//		return new ApiInfoBuilder()
//				.title("Admin API")
//				.contact("matchmakers")
//				.description("Matchmakers Administration Interface ")
//				.build();
//	}

	@Bean(name = "Matchmakers API")
	public Docket matchmakersAPIv1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Matchmakers API")
				.apiInfo(matchmakersInfo())
				.select()
				.paths(apiV1Paths())
				.build();
	}

	@Bean(name = "Matchmakers API v2")
	public Docket matchmakersAPIv2() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Matchmakers API v2")
				.apiInfo(matchmakersInfo())
				.select()
				.paths(apiV2Paths())
				.build();
	}

	private ApiInfo matchmakersInfo() {
		return new ApiInfoBuilder()
				.title("Matchmakers API")
				.contact("workable.com")
				.description("Workable's Candidate Matching Platform")
				.build();
	}

	private Predicate<String> apiV1Paths() {
		return or(regex("/(users|.*).*"));
	}

	private Predicate<String> apiV2Paths() {
		return or(regex("/v2/.*"));
	}
}
