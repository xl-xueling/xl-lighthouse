package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.insights.modal.RenderConfig;

public class StatExtendVO extends StatVO {

    private RenderConfig renderConfig;

    public StatExtendVO(StatVO stat) {
        super(stat);
    }

    public RenderConfig getRenderConfig() {
        return renderConfig;
    }

    public void setRenderConfig(RenderConfig renderConfig) {
        this.renderConfig = renderConfig;
    }
}
