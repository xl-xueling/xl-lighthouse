package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ComponentVerifyParam implements Serializable {

    @NotNull
    private ComponentTypeEnum type;

    @NotNull
    private String configuration;

    public ComponentTypeEnum getType() {
        return type;
    }

    public void setType(ComponentTypeEnum type) {
        this.type = type;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
