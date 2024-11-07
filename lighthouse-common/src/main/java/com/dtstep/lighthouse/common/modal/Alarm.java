package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.AlarmMatchEnum;
import com.dtstep.lighthouse.common.enums.CalculateMethod;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Alarm implements Serializable {

    private Integer id;

    private String title;

    private String uniqueCode;

    private boolean divide;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private boolean state;

    private AlarmMatchEnum match;

    private List<AlarmCondition> conditions;

    private Integer templateId;

    private boolean recover;

    private Integer delay;

    private long silent = TimeUnit.MINUTES.toSeconds(5);

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDivide() {
        return divide;
    }

    public void setDivide(boolean divide) {
        this.divide = divide;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public List<AlarmCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<AlarmCondition> conditions) {
        this.conditions = conditions;
    }

    public AlarmMatchEnum getMatch() {
        return match;
    }

    public void setMatch(AlarmMatchEnum match) {
        this.match = match;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public boolean isRecover() {
        return recover;
    }

    public void setRecover(boolean recover) {
        this.recover = recover;
    }

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }

    public long getSilent() {
        return silent;
    }

    public void setSilent(long silent) {
        this.silent = silent;
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
