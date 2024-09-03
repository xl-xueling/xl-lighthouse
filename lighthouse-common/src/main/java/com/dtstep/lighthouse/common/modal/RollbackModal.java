package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.RollbackStateEnum;
import com.dtstep.lighthouse.common.enums.RollbackTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RollbackModal implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer resourceId;

    private RollbackTypeEnum dataType;

    private String config;

    private Integer version;

    private RollbackStateEnum stateEnum;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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

    public RollbackStateEnum getStateEnum() {
        return stateEnum;
    }

    public void setStateEnum(RollbackStateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }
}
