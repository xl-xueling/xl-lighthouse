package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.modal.Alarm;

import java.util.List;

public class AlarmExtEntity extends Alarm {

    private List<AlarmTemplateExtEntity> templateList;

    public List<AlarmTemplateExtEntity> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<AlarmTemplateExtEntity> templateList) {
        this.templateList = templateList;
    }
}
