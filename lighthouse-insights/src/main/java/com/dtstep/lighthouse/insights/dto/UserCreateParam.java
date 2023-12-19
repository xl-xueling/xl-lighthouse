package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class UserCreateParam implements Serializable {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private Integer departmentId;

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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
