package com.workable.matchmakers.web.security.exception;

import org.springframework.security.core.AuthenticationException;

public class MatchmakersAuthenticationException extends AuthenticationException {

    public MatchmakersAuthenticationException(String message) {
        super(message);
    }
}
