package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;

import java.io.Serializable;

public class ResourceVO implements Serializable {

    private Integer id;

    private ResourceTypeEnum resourceType;

    private String title;

    private Object extend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
