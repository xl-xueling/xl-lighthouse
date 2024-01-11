package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;

public class ComponentCreateParam {

    private String title;

    private ComponentTypeEnum componentType;

    private String configuration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ComponentTypeEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentTypeEnum componentType) {
        this.componentType = componentType;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
