package com.dtstep.lighthouse.common.entity;

import java.util.List;

public class ViewStatBindsInfo {

    private List<ViewStatBindItem> binds;

    private String mode;

    private Integer interval;

    private List<LabelValue> dimens;

    public List<ViewStatBindItem> getBinds() {
        return binds;
    }

    public void setBinds(List<ViewStatBindItem> binds) {
        this.binds = binds;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public List<LabelValue> getDimens() {
        return dimens;
    }

    public void setDimens(List<LabelValue> dimens) {
        this.dimens = dimens;
    }
}
