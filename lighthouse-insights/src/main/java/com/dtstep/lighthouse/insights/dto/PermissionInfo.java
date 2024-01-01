package com.dtstep.lighthouse.insights.dto;

import java.util.ArrayList;
import java.util.List;

public class PermissionInfo {

    private List<PermissionEnum> permissions = new ArrayList<>();

    public List<PermissionEnum> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(PermissionEnum permissionEnum){
        this.permissions.add(permissionEnum);
    }

    public static enum PermissionEnum {
        readable,
        editable,
        approveable,
        retractable,

        ;
    }
}
