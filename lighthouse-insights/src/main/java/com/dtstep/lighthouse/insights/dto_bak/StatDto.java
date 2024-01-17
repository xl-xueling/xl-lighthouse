package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.Stat;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.ArrayList;
import java.util.List;

public class StatDto extends Stat {

    private Group group;

    private Project project;

    private List<User> admins;

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

    public StatDto(Stat stat){
        assert stat != null;
        BeanCopyUtil.copy(stat,this);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }
}
