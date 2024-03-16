package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.entity.view.LimitValue;

import java.util.List;

public class LimitDataObject {

    private Long batchTime;

    private String displayBatchTime;

    private List<LimitValue> values;

    public Long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Long batchTime) {
        this.batchTime = batchTime;
    }

    public List<LimitValue> getValues() {
        return values;
    }

    public void setValues(List<LimitValue> values) {
        this.values = values;
    }

    public String getDisplayBatchTime() {
        return displayBatchTime;
    }

    public void setDisplayBatchTime(String displayBatchTime) {
        this.displayBatchTime = displayBatchTime;
    }
}
