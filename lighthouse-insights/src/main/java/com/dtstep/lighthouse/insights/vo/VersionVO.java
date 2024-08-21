package com.dtstep.lighthouse.insights.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class VersionVO implements Serializable {

    private Integer resourceId;

    private Integer version;

    private LocalDateTime createTime;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
