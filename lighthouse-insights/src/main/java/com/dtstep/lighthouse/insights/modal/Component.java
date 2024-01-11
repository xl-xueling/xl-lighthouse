package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;

import java.time.LocalDateTime;

public class Component {

    private Integer id;

    private String title;

    private ComponentTypeEnum componentTypeEnum;

    private String configuration;

    private PrivateTypeEnum privateTypeEnum;

    private Integer userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ComponentTypeEnum getComponentTypeEnum() {
        return componentTypeEnum;
    }

    public void setComponentTypeEnum(ComponentTypeEnum componentTypeEnum) {
        this.componentTypeEnum = componentTypeEnum;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public PrivateTypeEnum getPrivateTypeEnum() {
        return privateTypeEnum;
    }

    public void setPrivateTypeEnum(PrivateTypeEnum privateTypeEnum) {
        this.privateTypeEnum = privateTypeEnum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
