package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.modal.RenderConfig;
import com.dtstep.lighthouse.insights.modal.Stat;

public class StatExtendDto extends StatDto {

    private RenderConfig renderConfig;

    public StatExtendDto(StatDto stat) {
        super(stat);
    }

    public RenderConfig getRenderConfig() {
        return renderConfig;
    }

    public void setRenderConfig(RenderConfig renderConfig) {
        this.renderConfig = renderConfig;
    }
}
