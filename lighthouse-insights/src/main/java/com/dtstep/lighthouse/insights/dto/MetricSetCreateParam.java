package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MetricSetCreateParam {

    @NotNull
    private String title;

    @NotNull
    private PrivateTypeEnum privateType;

    @NotNull
    private String desc;

    private GrantPermissionsParam permissionsParam;

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

    public GrantPermissionsParam getPermissionsParam() {
        return permissionsParam;
    }

    public void setPermissionsParam(GrantPermissionsParam permissionsParam) {
        this.permissionsParam = permissionsParam;
    }
}
