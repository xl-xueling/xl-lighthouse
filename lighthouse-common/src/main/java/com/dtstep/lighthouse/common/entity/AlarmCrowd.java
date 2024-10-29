package com.dtstep.lighthouse.common.entity;

import java.util.List;

public class AlarmCrowd {

    private List<Integer> userIds;

    private List<Integer> departmentIds;

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Integer> departmentIds) {
        this.departmentIds = departmentIds;
    }
}
