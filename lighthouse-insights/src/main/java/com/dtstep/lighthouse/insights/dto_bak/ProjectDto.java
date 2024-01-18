package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectDto extends Project {

    private List<User> admins;

    private Set<PermissionInfo.PermissionEnum> permissions = new HashSet<>();

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

    public Set<PermissionInfo.PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionInfo.PermissionEnum> permissions) {
        this.permissions = permissions;
    }
}
