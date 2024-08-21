package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.RollbackTypeEnum;

public class RollbackQueryParam {

    private Integer resourceId;

    private RollbackTypeEnum dataType;

    private Integer version;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public RollbackTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(RollbackTypeEnum dataType) {
        this.dataType = dataType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
