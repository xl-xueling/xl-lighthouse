package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Component;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.ArrayList;
import java.util.List;

public class ComponentDto extends Component {

    private User user;

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

    public ComponentDto(Component component){
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
