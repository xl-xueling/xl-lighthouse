package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;

public class ViewFavoriteQueryParam implements Serializable {

    private Integer cateId;

    private String search;

    private Integer userId;

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
}
