package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.role.PermissionsEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.ArrayList;
import java.util.List;

public class ProjectDto extends Project {

    private List<User> admins;

    private List<CommonTreeNode> structure;

    private List<PermissionInfo.PermissionEnum> permissions = new ArrayList<>();

    public List<PermissionInfo.PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionInfo.PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(PermissionInfo.PermissionEnum permissionEnum){
        if(permissionEnum != null && !this.permissions.contains(permissionEnum)){
            this.permissions.add(permissionEnum);
        }
    }

    public ProjectDto(Project project){
        assert project != null;
        BeanCopyUtil.copy(project,this);
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<CommonTreeNode> getStructure() {
        return structure;
    }

    public void setStructure(List<CommonTreeNode> structure) {
        this.structure = structure;
    }
}
