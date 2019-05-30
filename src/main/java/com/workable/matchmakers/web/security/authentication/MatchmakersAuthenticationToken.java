package com.workable.matchmakers.web.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class MatchmakersAuthenticationToken extends AbstractAuthenticationToken {

    public MatchmakersAuthenticationToken() {
        super(Collections.emptyList());
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public MatchmakersAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    private Object token;
    private Object user;

    public void setCredentials(Object token) {
        this.token = token;
    }

    public void setPrincipal(Object user) {
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
