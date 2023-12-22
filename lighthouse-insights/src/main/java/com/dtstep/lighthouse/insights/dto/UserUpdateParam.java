package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class UserUpdateParam implements Serializable {

    @NotEmpty
    private Integer id;

    private Integer departmentId;

    private String phone;

    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
