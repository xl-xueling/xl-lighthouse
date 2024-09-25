package com.dtstep.lighthouse.insights.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CallerCreateParam {

    @NotEmpty
    private String name;

    @NotEmpty
    private String desc;

    private List<Integer> usersPermission;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Integer> getUsersPermission() {
        return usersPermission;
    }

    public void setUsersPermission(List<Integer> usersPermission) {
        this.usersPermission = usersPermission;
    }
}
