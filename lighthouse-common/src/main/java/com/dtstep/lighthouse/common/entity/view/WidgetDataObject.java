package com.dtstep.lighthouse.common.entity.view;

import com.dtstep.lighthouse.common.modal.DebugParam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class WidgetDataObject implements Serializable {

    private String widgetKey;

    private List<StatDataSeries> data;

    private Map<String,List<StatDataSeries>> contrastData;

    private List<DebugParam> debugParams;

    private String errorMessage;

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, List<StatDataSeries>> getContrastData() {
        return contrastData;
    }

    public void setContrastData(Map<String, List<StatDataSeries>> contrastData) {
        this.contrastData = contrastData;
    }
}
