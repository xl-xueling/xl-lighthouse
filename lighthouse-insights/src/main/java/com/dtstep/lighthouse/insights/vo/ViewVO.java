package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.modal.View;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewVO extends View {

    private List<User> admins;

    private Object config;

    private Map<String,List<TreeNode>> filtersData;

    private List<ResourceVO> resourceList;

    public Map<String, List<TreeNode>> getFiltersData() {
        return filtersData;
    }

    public void setFiltersData(Map<String, List<TreeNode>> filtersData) {
        this.filtersData = filtersData;
    }

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

    public ViewVO(View view){
        assert view != null;
        BeanCopyUtil.copy(view,this);
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public List<ResourceVO> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourceVO> resourceList) {
        this.resourceList = resourceList;
    }
}
