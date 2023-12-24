package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.role.PermissionsEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.List;

public class ProjectDto extends Project {

    private Department department;

    private List<User> admins;

    private List<CommonTreeNode> structure;

    private List<PermissionsEnum> permissions;

    public ProjectDto(Project project){
        assert project != null;
        BeanCopyUtil.copy(project,this);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<PermissionsEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionsEnum> permissions) {
        this.permissions = permissions;
    }

    public List<CommonTreeNode> getStructure() {
        return structure;
    }

    public void setStructure(List<CommonTreeNode> structure) {
        this.structure = structure;
    }
}
