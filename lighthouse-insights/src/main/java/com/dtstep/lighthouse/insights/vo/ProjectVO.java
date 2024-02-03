package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.modal.Project;
import com.dtstep.lighthouse.common.modal.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectVO extends Project {

    private List<User> admins;

    private TreeNode structure;

    private Set<PermissionEnum> permissions = new HashSet<>();

    public Set<PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(PermissionEnum permission){
        if(permission != null){
            permissions.add(permission);
        }
    }

    public ProjectVO(Project project){
        assert project != null;
        BeanCopyUtil.copy(project,this);
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public TreeNode getStructure() {
        return structure;
    }

    public void setStructure(TreeNode structure) {
        this.structure = structure;
    }
}
