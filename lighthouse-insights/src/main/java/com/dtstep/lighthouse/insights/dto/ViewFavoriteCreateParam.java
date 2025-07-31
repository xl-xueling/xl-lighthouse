package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.ViewElementType;

import java.io.Serializable;

public class ViewFavoriteCreateParam implements Serializable {

    private Integer categoryId;

    private String name;

    private ViewElementType elementType;

    private Object config;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public ViewElementType getElementType() {
        return elementType;
    }

    public void setElementType(ViewElementType elementType) {
        this.elementType = elementType;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }
}
