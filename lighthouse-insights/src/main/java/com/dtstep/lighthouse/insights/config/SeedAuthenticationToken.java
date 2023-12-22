package com.dtstep.lighthouse.insights.config;

import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SeedAuthenticationToken extends AbstractAuthenticationToken {

    private Integer userId;

    private String username;

    private String seed;

    public SeedAuthenticationToken(Integer userId,String username,String seed){
        super(Lists.newArrayList());
        this.userId = userId;
        this.username = username;
        this.seed = seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSeed() {
        return seed;
    }

    @Override
    public Integer getPrincipal() {
        return userId;
    }

    @Override
    public Object getCredentials() {
        return seed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
