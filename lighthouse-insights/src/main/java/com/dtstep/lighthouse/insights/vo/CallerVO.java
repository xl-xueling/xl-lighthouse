package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.modal.MetricSet;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallerVO extends Caller {

    private List<User> admins;

    private Set<PermissionEnum> permissions = new HashSet<>();

    public CallerVO(Caller caller){
        Validate.notNull(caller);
        BeanCopyUtil.copy(caller,this);
    }

    public void addPermission(PermissionEnum permission){
        if(permission != null){
            permissions.add(permission);
        }
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public Set<PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEnum> permissions) {
        this.permissions = permissions;
    }
}
