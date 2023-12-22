package com.dtstep.lighthouse.insights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePasswordParam implements Serializable {

    @NotNull
    private Integer id;

    @NotEmpty
    @Size(min = 6,max = 50)
    private String originPassword;

    @NotEmpty
    @Size(min = 6,max = 50)
    private String password;

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
}
