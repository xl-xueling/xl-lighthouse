package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.modal.AlarmCondition;

import java.util.List;

public class AlarmCreateParam {

    private String title;

    private boolean divide;

    private String desc;

    private List<String> dimens;

    private boolean status;

    private Integer templateId;

    private String uniqueCode;

    private Integer delay;

    private List<AlarmCondition> conditions;

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

    public List<String> getDimens() {
        return dimens;
    }

    public void setDimens(List<String> dimens) {
        this.dimens = dimens;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
}
