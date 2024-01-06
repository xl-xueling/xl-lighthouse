package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto extends User {

    private List<PermissionInfo.PermissionEnum> permissions = new ArrayList<>();

    public UserDto(User user){
        assert user != null;
        BeanCopyUtil.copy(user,this);
    }

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
}
