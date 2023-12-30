package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class LoginParam {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,15}$")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,32}$")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
