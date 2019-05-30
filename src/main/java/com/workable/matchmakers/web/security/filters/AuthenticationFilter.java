package com.workable.matchmakers.web.security.filters;

import com.workable.matchmakers.web.security.authentication.MatchmakersAuthenticationDetailsSource;
import com.workable.matchmakers.web.security.authentication.MatchmakersAuthenticationToken;
import com.workable.matchmakers.web.security.authentication.AuthenticationDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private MatchmakersAuthenticationDetailsSource matchmakersAuthenticationDetailsSource;
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authManager) {
        this.authenticationManager = authManager;
        this.matchmakersAuthenticationDetailsSource = new MatchmakersAuthenticationDetailsSource();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (authenticate(httpRequest)) {
            chain.doFilter(request, response);
        }
    }

    private boolean authenticate(HttpServletRequest request) {
        // Build AuthenticationDetails parsing HTTP Authorization header
        AuthenticationDetails authDetails = this.matchmakersAuthenticationDetailsSource.buildDetails(request);

        // Build matchmakersAuthenticationToken using AuthenticationDetails
        MatchmakersAuthenticationToken authToken = new MatchmakersAuthenticationToken();
        authToken.setDetails(authDetails);

        // Authenticate request using the list of the authentication manager's authentication providers (matchmakersAuthenticationProvider)
        Authentication authResult = this.authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        return true;
    }
}
