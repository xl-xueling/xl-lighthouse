package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.dto_bak.PermissionEnum;
import com.dtstep.lighthouse.insights.modal.Component;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComponentVO extends Component {

    private User user;

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

    public ComponentVO(Component component){
        assert component != null;
        BeanCopyUtil.copy(component,this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
