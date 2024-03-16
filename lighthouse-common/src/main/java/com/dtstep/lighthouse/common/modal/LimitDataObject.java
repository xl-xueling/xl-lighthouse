package com.dtstep.lighthouse.common.modal;

import java.util.List;

public class LimitDataObject {

    private Long batchTime;

    private List<KV<String,Object>> values;

    public Long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Long batchTime) {
        this.batchTime = batchTime;
    }

    public List<KV<String, Object>> getValues() {
        return values;
    }

    public void setValues(List<KV<String, Object>> values) {
        this.values = values;
    }
}
