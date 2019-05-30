package com.workable.matchmakers.web.validator;

import com.workable.matchmakers.web.support.SecurityHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {


    @Value("${application.roles.admin.access-token}")
    private String adminAccessToken;

    public void isAdminToken(String authorization) {
        String token = SecurityHelper.getAccessToken(authorization);

        if (!adminAccessToken.equalsIgnoreCase(token)) {
            throw new AuthorizationServiceException("Unauthorized access. Invalid administration token!");
        }
    }
}
