package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.ComponentTypeEnum;

import java.util.List;

public class RenderFilterConfig {

    private ComponentTypeEnum componentType;

    private String title;

    private String label;

    private String dimens;

    private Integer componentId;

    private List<TreeNode> configData;

    public ComponentTypeEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentTypeEnum componentType) {
        this.componentType = componentType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public List<TreeNode> getConfigData() {
        return configData;
    }

    public void setConfigData(List<TreeNode> configData) {
        this.configData = configData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
