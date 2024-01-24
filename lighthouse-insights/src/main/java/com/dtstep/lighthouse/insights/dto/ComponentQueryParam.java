package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;

public class ComponentQueryParam {

    private Integer id;

    private PrivateTypeEnum privateType;

    private Integer userId;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
