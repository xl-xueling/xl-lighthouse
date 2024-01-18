package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.dto_bak.PermissionInfo;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserVO extends User {

    public UserVO(User user){
        assert user != null;
        BeanCopyUtil.copy(user,this);
    }

    private Set<PermissionInfo.PermissionEnum> permissions = new HashSet<>();

    public Set<PermissionInfo.PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionInfo.PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(PermissionInfo.PermissionEnum permission){
        if(permission != null){
            permissions.add(permission);
        }
    }
}
