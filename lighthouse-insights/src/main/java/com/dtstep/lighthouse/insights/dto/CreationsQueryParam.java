package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.CreationsTypeEnum;

import java.io.Serializable;

public class CreationsQueryParam implements Serializable {

    private Integer cateId;

    private String search;

    private Integer userId;

    private CreationsTypeEnum type;

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
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

    public CreationsTypeEnum getType() {
        return type;
    }

    public void setType(CreationsTypeEnum type) {
        this.type = type;
    }
}
