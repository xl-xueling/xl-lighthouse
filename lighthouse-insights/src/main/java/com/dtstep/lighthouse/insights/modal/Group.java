package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Group implements Serializable {

    private Integer id;

    @NotEmpty
    private String token;

    @NotNull
    private Integer projectId;

    @NotEmpty
    private List<Column> columns;

    private String secretKey;

    private GroupStateEnum state;

    @NotEmpty
    private String desc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime refreshTime;

    private GroupExtendConfig extendConfig;

    private Integer debugMode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public GroupStateEnum getState() {
        return state;
    }

    public void setState(GroupStateEnum state) {
        this.state = state;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public GroupExtendConfig getExtendConfig() {
        return extendConfig;
    }

    public void setExtendConfig(GroupExtendConfig extendConfig) {
        this.extendConfig = extendConfig;
    }

    public Integer getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(Integer debugMode) {
        this.debugMode = debugMode;
    }

    public LocalDateTime getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(LocalDateTime refreshTime) {
        this.refreshTime = refreshTime;
    }
}
