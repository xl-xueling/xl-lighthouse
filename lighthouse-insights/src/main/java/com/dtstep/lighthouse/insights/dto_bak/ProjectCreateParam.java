package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.insights.modal.Project;

import java.util.List;

public class ProjectCreateParam extends Project {

    private List<Integer> usersPermission;

    private List<Integer> departmentsPermission;

    public List<Integer> getUsersPermission() {
        return usersPermission;
    }

    public void setUsersPermission(List<Integer> usersPermission) {
        this.usersPermission = usersPermission;
    }

    public List<Integer> getDepartmentsPermission() {
        return departmentsPermission;
    }

    public void setDepartmentsPermission(List<Integer> departmentsPermission) {
        this.departmentsPermission = departmentsPermission;
    }
}
