package com.dtstep.lighthouse.insights.modal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupExtendConfig implements Serializable {

    private List<LimitedConfig> limitedConfig;

    private DebugConfig debugConfig;

    public DebugConfig getDebugConfig() {
        return debugConfig;
    }

    public void setDebugConfig(DebugConfig debugConfig) {
        this.debugConfig = debugConfig;
    }

    public List<LimitedConfig> getLimitedConfig() {
        return limitedConfig;
    }

    public void setLimitedConfig(List<LimitedConfig> limitedConfig) {
        this.limitedConfig = limitedConfig;
    }
}
