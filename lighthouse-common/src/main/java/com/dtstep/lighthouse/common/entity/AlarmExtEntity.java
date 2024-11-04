package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

import java.util.List;

public class AlarmExtEntity extends Alarm {

    private List<AlarmTemplateExtEntity> templateList;

    public AlarmExtEntity(){}

    public AlarmExtEntity(Alarm alarm){
        assert alarm != null;
        BeanCopyUtil.copy(alarm,this);
    }

    public List<AlarmTemplateExtEntity> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<AlarmTemplateExtEntity> templateList) {
        this.templateList = templateList;
    }
}
