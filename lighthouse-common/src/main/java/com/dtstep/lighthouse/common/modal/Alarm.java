package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.AlarmMatchEnum;
import com.dtstep.lighthouse.common.enums.AlarmStateEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Alarm implements Serializable {

    private Integer id;

    private String title;

    private String uniqueCode;

    private boolean divide;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private AlarmStateEnum state;

    private AlarmMatchEnum match;

    private List<AlarmCondition> conditions;

    private Integer templateId;

    private Integer delay;

    private String desc;

    private String dimens;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public AlarmStateEnum getState() {
        return state;
    }

    public void setState(AlarmStateEnum state) {
        this.state = state;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
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

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public boolean isDivide() {
        return divide;
    }

    public void setDivide(boolean divide) {
        this.divide = divide;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AlarmMatchEnum getMatch() {
        return match;
    }

    public void setMatch(AlarmMatchEnum match) {
        this.match = match;
    }

    public List<AlarmCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<AlarmCondition> conditions) {
        this.conditions = conditions;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }
}
