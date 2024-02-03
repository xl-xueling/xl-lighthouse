package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MetricSetCreateParam {

    @NotNull
    private String title;

    @NotNull
    private PrivateTypeEnum privateType;

    @NotNull
    private String desc;

    private List<Integer> initUsersPermission;

    private List<Integer> initDepartmentsPermission;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Integer> getInitUsersPermission() {
        return initUsersPermission;
    }

    public void setInitUsersPermission(List<Integer> initUsersPermission) {
        this.initUsersPermission = initUsersPermission;
    }

    public List<Integer> getInitDepartmentsPermission() {
        return initDepartmentsPermission;
    }

    public void setInitDepartmentsPermission(List<Integer> initDepartmentsPermission) {
        this.initDepartmentsPermission = initDepartmentsPermission;
    }
}
