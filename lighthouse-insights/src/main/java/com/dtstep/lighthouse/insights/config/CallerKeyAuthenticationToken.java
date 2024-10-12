package com.dtstep.lighthouse.insights.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CallerKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final Integer callerId;

    private final String callerKey;

    public CallerKeyAuthenticationToken(Integer callerId, String callerKey) {
        super(null);
        this.callerId = callerId;
        this.callerKey = callerKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return callerKey;
    }

    @Override
    public Object getPrincipal() {
        return callerId;
    }
}
