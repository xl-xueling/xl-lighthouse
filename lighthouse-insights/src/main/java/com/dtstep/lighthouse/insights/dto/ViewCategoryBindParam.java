package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.ViewElementType;

import java.io.Serializable;

public class ViewCategoryBindParam implements Serializable {

    private Integer categoryId;

    private ViewElementType elementType;

    private String config;

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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
