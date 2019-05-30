package com.workable.matchmakers.web.security.authentication;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class MatchmakersAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, AuthenticationDetails> {

    @Override
    public AuthenticationDetails buildDetails(HttpServletRequest httpRequest) {
        AuthenticationDetails authDetails = new AuthenticationDetails();

        String contextPath = httpRequest.getServletPath();
        authDetails.setContextPath(contextPath);

        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (StringUtils.isNotBlank(authorizationHeader)) {
            String accessToken = StringUtils.substringAfter(authorizationHeader, "Bearer ");
            authDetails.setAccessToken(accessToken);
        }

        return authDetails;
    }
}
