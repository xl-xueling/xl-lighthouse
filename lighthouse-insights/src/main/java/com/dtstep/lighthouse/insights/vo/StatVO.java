package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.modal.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatVO extends Stat {

    private List<User> admins;

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

    public StatVO(Stat stat){
        assert stat != null;
        BeanCopyUtil.copy(stat,this);
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }
}
