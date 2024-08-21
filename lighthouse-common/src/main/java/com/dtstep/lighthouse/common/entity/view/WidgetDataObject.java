package com.dtstep.lighthouse.common.entity.view;

import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.modal.DebugParam;

import java.io.Serializable;
import java.util.List;

public class WidgetDataObject implements Serializable {

    private String widgetKey;

    private List<StatDataSeries> data;

    private List<DebugParam> debugParams;

    private ResultCode resultCode;

    public String getWidgetKey() {
        return widgetKey;
    }

    public void setWidgetKey(String widgetKey) {
        this.widgetKey = widgetKey;
    }

    public List<StatDataSeries> getData() {
        return data;
    }

    public void setData(List<StatDataSeries> data) {
        this.data = data;
    }

    public List<DebugParam> getDebugParams() {
        return debugParams;
    }

    public void setDebugParams(List<DebugParam> debugParams) {
        this.debugParams = debugParams;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
