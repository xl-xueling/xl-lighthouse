package com.dtstep.lighthouse.insights.config;

import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SeedAuthenticationToken extends AbstractAuthenticationToken {

    private String seed;

    public SeedAuthenticationToken(String seed){
        super(Lists.newArrayList());
        this.seed = seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    @Override
    public String getPrincipal() {
        return seed;
    }

    @Override
    public Object getCredentials() {
        return seed;
    }
}
