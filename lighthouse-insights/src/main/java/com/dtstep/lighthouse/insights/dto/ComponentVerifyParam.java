package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ComponentVerifyParam implements Serializable {

    @NotNull
    private ComponentTypeEnum componentType;

    @NotNull
    private String configuration;

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
