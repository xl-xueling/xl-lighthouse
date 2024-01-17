package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;

import javax.validation.constraints.NotNull;

public class ComponentCreateParam {

    @NotNull
    private String title;

    @NotNull
    private ComponentTypeEnum componentType;

    @NotNull
    private PrivateTypeEnum privateType;

    @NotNull
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

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }
}
