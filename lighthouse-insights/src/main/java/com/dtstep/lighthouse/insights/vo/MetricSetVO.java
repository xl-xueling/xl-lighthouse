package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.MetricBindElement;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.MetricSet;
import com.dtstep.lighthouse.common.modal.User;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetricSetVO extends MetricSet {

    private List<User> admins;

    private List<MetricBindElement> bindElements;

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

    public MetricSetVO(MetricSet metricSet){
        Validate.notNull(metricSet);
        BeanCopyUtil.copy(metricSet,this);
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<MetricBindElement> getBindElements() {
        return bindElements;
    }

    public void setBindElements(List<MetricBindElement> bindElements) {
        this.bindElements = bindElements;
    }
}
