package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;

public class RenderDateConfig {

    private ComponentTypeEnum renderType;

    private String label;

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
}
