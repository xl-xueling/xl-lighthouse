package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class ChangePasswordParam implements Serializable {

    @NotNull
    private Integer id;

    @NotEmpty
    private String username;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,32}$")
    private String originPassword;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,32}$")
    private String password;

    public ChangePasswordParam(){}

    public ChangePasswordParam(Integer id,String originPassword,String password){
        this.id = id;
        this.originPassword = originPassword;
        this.password = password;
    }

    public ChangePasswordParam(Integer id,String password){
        this.id = id;
        this.originPassword = originPassword;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
