package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PermissionGrantParam {

    @NotNull
    private Integer resourceId;

    @NotNull
    private RoleTypeEnum roleType;

    private List<Integer> usersPermissions;

    private List<Integer> departmentsPermissions;

    public List<Integer> getUsersPermissions() {
        return usersPermissions;
    }

    public void setUsersPermissions(List<Integer> usersPermissions) {
        this.usersPermissions = usersPermissions;
    }

    public List<Integer> getDepartmentsPermissions() {
        return departmentsPermissions;
    }

    public void setDepartmentsPermissions(List<Integer> departmentsPermissions) {
        this.departmentsPermissions = departmentsPermissions;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public RoleTypeEnum getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypeEnum roleType) {
        this.roleType = roleType;
    }
}
