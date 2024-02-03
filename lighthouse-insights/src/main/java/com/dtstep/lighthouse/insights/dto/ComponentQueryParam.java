package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;

public class ComponentQueryParam {

    private Integer id;

    private PrivateTypeEnum privateType;

    private Integer userId;

    private String search;

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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
