package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.dto.TreeNode;
import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;

import java.util.List;

public class RenderFilterConfig {

    private ComponentTypeEnum renderType;

    private String label;

    private String dimens;

    private Integer componentId;

    private List<TreeNode> configData;

    public ComponentTypeEnum getRenderType() {
        return renderType;
    }

    public void setRenderType(ComponentTypeEnum renderType) {
        this.renderType = renderType;
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
}
