package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.dto.TreeNode;
import com.dtstep.lighthouse.insights.enums.RenderTypeEnum;

import java.util.List;

public class RenderDateConfig {

    private RenderTypeEnum renderType;

    private String label;

    public RenderTypeEnum getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderTypeEnum renderType) {
        this.renderType = renderType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
