package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;

import javax.validation.constraints.NotNull;

public class MetricSetUpdateParam {

    @NotNull
    private Integer id;

    @NotNull
    private String title;

    @NotNull
    private PrivateTypeEnum privateType;

    @NotNull
    private String desc;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
